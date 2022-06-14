package com.mx.path.gateway.context

import java.util.function.Supplier

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mx.common.security.EncryptionService
import com.mx.models.MdxList
import com.mx.models.account.Account
import com.mx.path.model.context.AccountBehavior
import com.mx.path.model.context.Session
import com.mx.testing.TestEncryptionService
import com.mx.testing.Utils
import com.mx.testing.WithSessionRepository

import spock.lang.Specification

class SessionAccountCacheTest extends Specification implements WithSessionRepository {
  class SpecialAccount extends Account {
    boolean newType = true;

    def getNewType() {
      newType
    }

    def setNewType(newType) {
      this.newType = newType
    }
  }

  Supplier<EncryptionService> previousEncryptionServiceSupplier;
  MdxList<Account> accounts

  def setup() {
    Utils.injectSessionEncryptionService()
    Session.createSession()
    accounts = new MdxList<SpecialAccount>().tap {
      add(new SpecialAccount().tap {
        setId("account123")
        setBalance(10.00)
      })
    }
  }

  def cleanup() {
    Session.clearSession()
    Utils.resetSessionEncryptionService()
  }

  def "loads from session"() {
    when:
    def cache = new SessionAccountCache<>(Session.current())

    then:
    cache.class == SessionAccountCache.class
  }

  def "set accounts"() {
    given:
    def cache = new SessionAccountCache<>(Session.current())

    when:
    cache.setAccounts(accounts)

    then:
    cache.isValid()
  }

  def "get special accounts"() {
    given:
    def cache = new SessionAccountCache<>(Session.current())

    when:
    cache.setAccounts(accounts)

    then:
    cache.isValid()
    cache.getAccounts(new TypeToken<MdxList<SpecialAccount>>(){}.getType()).size() == 1

    when:
    cache.invalidate()

    then:
    !cache.isValid()
    cache.getAccounts(new TypeToken<MdxList<SpecialAccount>>(){}.getType()) == null
  }

  def "get accounts"() {
    given:
    def cache = new SessionAccountCache<>(Session.current())

    when:
    cache.setAccounts(accounts)

    then:
    cache.isValid()
    cache.getAccounts().size() == 1

    when:
    cache.invalidate()

    then:
    !cache.isValid()
    cache.getAccounts() == null
  }

  def "account supplier"() {
    when:
    def cache = new SessionAccountCache<>(Session.current()).withAccountSupplier({ return accounts })

    then:
    accounts == cache.getAccounts()

    when: "accounts are null and still valid"
    cache.setAccounts(null)

    then:
    cache.isValid()

    and: "the supplier is called and is validated"
    accounts == cache.getAccounts()
    cache.isValid()
  }

  def "junk"() {
    given:
    Gson g = new Gson()

    def a = new ArrayList<AccountBehavior>()
    a.add(new AccountBehavior().tap{
      setCanTransferFrom(true)
      setCanTransferTo(false)
    })

    def type = new TypeToken<List<AccountBehavior>>() {}.getType()
    def json = g.toJson(a)
    def de = g.fromJson(json, type)
  }
}