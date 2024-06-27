package com.mx.gateway


import com.mx.testing.AccountAccessorImpl
import com.mx.testing.BaseAccessorImpl
import com.mx.testing.gateway.api.AccountGateway
import com.mx.testing.model.Account

import spock.lang.Specification

/**
 * These tests are intended to check that the internals of the generated gateway methods work correctly and don't
 * encounter any casting issues.
 */
class GatewayMethodSpec extends Specification {
  AccountGateway subject

  def setup() {
    def baseAccessor = new BaseAccessorImpl()
    baseAccessor.setAccounts(new AccountAccessorImpl())
    subject = AccountGateway.builder().baseAccessor(baseAccessor).build()
  }

  def "invocation of list type"() {
    when:
    def result = subject.get20240101("123")

    then:
    result != null
    result.result instanceof com.mx.testing.models.v20240101.Account

    when:
    result = subject.get("123")

    then:
    result != null
    result.result instanceof Account
  }

  def "invoke create"() {
    when:
    def result = subject.create(new com.mx.testing.models.v20240101.Account())

    then:
    result != null
    result.result instanceof com.mx.testing.models.v20240101.Account

    when:
    result = subject.create(new Account())

    then:
    result != null
    result.result instanceof Account
  }
}