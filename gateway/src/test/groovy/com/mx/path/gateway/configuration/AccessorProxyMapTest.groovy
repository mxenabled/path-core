package com.mx.path.gateway.configuration

import com.mx.common.exception.GatewayException
import com.mx.testing.AccountAccessorImpl
import com.mx.testing.BaseAccessorImpl
import com.mx.testing.ChildAccessorAccount
import com.mx.testing.TransactionAccessorImpl

import spock.lang.Specification

class AccessorProxyMapTest extends Specification {
  def cleanup() {
    AccessorProxyMap.reset()
  }

  def "#add"() {
    when:
    AccessorProxyMap.add("singleton", BaseAccessorImpl.class, AccountAccessorImpl.class)

    then:
    AccessorProxyMap.get("singleton", BaseAccessorImpl) == AccountAccessorImpl
  }

  def "#freeze"() {
    given:
    AccessorProxyMap.add("singleton", BaseAccessorImpl.class, AccountAccessorImpl.class)
    AccessorProxyMap.freeze()

    when:
    def result = AccessorProxyMap.get("singleton", BaseAccessorImpl.class)

    then: "can be read"
    result == AccountAccessorImpl

    when:
    AccessorProxyMap.add("prototype", BaseAccessorImpl.class, ChildAccessorAccount.class)

    then: "error is thrown"
    def ex = thrown(GatewayException)
    ex.message == "Attempting to modify frozen Accessor Proxy Mappings"
  }

  def "#get"() {
    given:
    AccessorProxyMap.add("singleton", BaseAccessorImpl.class, AccountAccessorImpl.class)
    AccessorProxyMap.add("prototype", BaseAccessorImpl.class, ChildAccessorAccount.class)

    when:
    def result = [
      AccessorProxyMap.get("singleton", BaseAccessorImpl.class),
      AccessorProxyMap.get("prototype", BaseAccessorImpl.class)
    ]
    then:
    result == [
      AccountAccessorImpl.class,
      ChildAccessorAccount.class
    ]

    when:
    result = [
      AccessorProxyMap.get("SINGLETON", BaseAccessorImpl.class),
      AccessorProxyMap.get("ProtoType", BaseAccessorImpl.class)
    ]
    then: "scope is case-insensitive"
    result == [
      AccountAccessorImpl.class,
      ChildAccessorAccount.class
    ]

    when: "proxy does not exist"
    AccessorProxyMap.get("singleton", TransactionAccessorImpl.class)

    then: "GatewayException is thrown"
    def ex = thrown(GatewayException)
    ex.getMessage() == "No proxies for accessor type com.mx.testing.TransactionAccessorImpl"

    when: "scope not defined"
    AccessorProxyMap.get("incorrect", BaseAccessorImpl.class)

    then: "GatewayException is thrown"
    ex = thrown(GatewayException)
    ex.getMessage() == "No proxies for scope incorrect"
  }
}
