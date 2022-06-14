package com.mx.path.api

import com.mx.models.account.Account
import com.mx.testing.MethodTest

import spock.lang.Specification

class ApiMethodTest extends Specification {

  def list
  def get
  def invalidReturnType
  def invalidReturnListType

  void setup() {
    list = new ApiMethod(MethodTest.class.getMethod("list"))
    get = new ApiMethod(MethodTest.class.getMethod("get"))
    invalidReturnType = new ApiMethod(MethodTest.class.getMethod("invalidReturnType"))
    invalidReturnListType = new ApiMethod(MethodTest.class.getMethod("invalidReturnListType"))
  }

  def "isListOp"() {
    when: "single"
    def isListOp = get.isListOp()

    then:
    !isListOp

    when: "list"
    isListOp = list.isListOp()

    then:
    isListOp
  }

  def "isValid"() {
    when: "single"
    def isValid = get.isValid()

    then:
    isValid

    when: "list"
    isValid = list.isValid()

    then:
    isValid

    when: "invalid"
    isValid = invalidReturnType.isValid()

    then:
    !isValid

    when: "invalid"
    isValid = invalidReturnListType.isValid()

    then:
    !isValid
  }

  def "getModel"() {
    when: "single"
    def t = get.getModel()

    then:
    t == Account.class

    when: "list"
    t = list.getModel()

    then:
    t == Account.class
  }

  def "getName"() {
    when:
    true

    then:
    list.getName() == "list"
    get.getName() == "get"
    invalidReturnType.getName() == "invalidReturnType"
    invalidReturnListType.getName() == "invalidReturnListType"
  }

  def "getParameterizedReturnType"() {
    when:
    true

    then:
    get.getParameterizedReturnType() == "Account.class"
    list.getParameterizedReturnType() == "MdxList.ofClass(Account.class)"
  }
}
