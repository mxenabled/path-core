package com.mx.path.model.context

import com.mx.common.collections.ObjectMap
import com.mx.testing.EncryptionServiceImpl
import com.mx.testing.HashSessionRepository
import com.mx.testing.WithEncryptionService

import spock.lang.Specification

class AccountBehaviorsTest extends Specification implements WithEncryptionService {

  def setup() {
    HashSessionRepository.register()
    Session.createSession()
    Session.setEncryptionServiceSupplier({ -> new EncryptionServiceImpl(new ObjectMap()) })
  }

  def cleanup() {
    Session.setRepositorySupplier(null)
    Session.clearSession()
    Session.setEncryptionServiceSupplier(null)
  }

  def "accountBehaviorsWriteReadSession"() {
    given:
    def accountBehaviors = new AccountBehaviors()
    def accountBehavior = new AccountBehavior()
    def idMappings = new HashMap<Session.ServiceIdentifier, String>()
    def accountOwners = new ArrayList<SessionAccountOwner>()
    idMappings.put(Session.ServiceIdentifier.Architect, "123account")
    accountBehavior.setMappedAccountIds(idMappings)
    accountOwners.add(new SessionAccountOwner())
    accountBehavior.setAccountOwners(accountOwners)
    accountBehavior.setAccountSuffix("suffix")
    accountBehavior.setAccountType("accountType")
    accountBehavior.setCanReceiveRemoteDeposit(true)
    accountBehavior.setExtAccountType("extAccountType")
    accountBehavior.setMaskedAccountNumber("123456789")
    accountBehavior.setServiceAccountId("987654321")
    accountBehavior.setCanTransferFrom(false)
    accountBehavior.setCanTransferTo(true)
    accountBehaviors.put("account123", accountBehavior)

    when:
    accountBehaviors.saveToSession()

    then:
    "123account" == AccountBehaviors.loadFromSession().get("account123").getMappedAccountIds().get(Session.ServiceIdentifier.Architect)
  }

  def "verifyServiceAccountIdMapsToMdxAccountId"() {
    given:
    def accountBehaviors = new AccountBehaviors()
    def accountBehavior = new AccountBehavior()
    def serviceAccountId = "123account"
    def mdxAccountId = "account123"
    def idMappings = new HashMap<Session.ServiceIdentifier, String>()
    idMappings.put(Session.ServiceIdentifier.Checkfree, serviceAccountId)
    accountBehavior.setMappedAccountIds(idMappings)
    accountBehaviors.put(mdxAccountId, accountBehavior)

    when:
    accountBehaviors.saveToSession()

    then:
    mdxAccountId == AccountBehaviors.loadFromSession().serviceAccountIdToAccountId(Session.ServiceIdentifier.Checkfree, serviceAccountId)
  }

  def "verifyNullOnExternalGuidLookup"() {
    given:
    def accountBehaviors = new AccountBehaviors()
    def accountBehavior = new AccountBehavior()
    def serviceAccountId = "123account"
    def mdxAccountId = "account123"
    def badServiceAccountId = "123accounts"
    def idMappings = new HashMap<Session.ServiceIdentifier, String>()
    idMappings.put(Session.ServiceIdentifier.Checkfree, serviceAccountId)
    accountBehavior.setMappedAccountIds(idMappings)
    accountBehaviors.put(mdxAccountId, accountBehavior)
    accountBehaviors.saveToSession()

    expect:
    AccountBehaviors.loadFromSession().serviceAccountIdToAccountId(Session.ServiceIdentifier.Checkfree, badServiceAccountId) == null
  }
}