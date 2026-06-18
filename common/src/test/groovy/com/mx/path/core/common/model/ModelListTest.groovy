package com.mx.path.core.common.model


import com.mx.testing.TestAccount

import spock.lang.Specification

class ModelListTest extends Specification {

  static class ChildTestAccount extends TestAccount {}

  def "add, get, size, isEmpty"() {
    given:
    def list = new ModelList<TestAccount>()

    expect:
    list.isEmpty()
    list.size() == 0

    when:
    list.add(new TestAccount())

    then:
    !list.isEmpty()
    list.size() == 1
    list.get(0) != null
  }

  def "remove by index and by object"() {
    given:
    def list = new ModelList<TestAccount>()
    def account = new TestAccount()
    list.add(account)

    when:
    def removed = list.remove(0)

    then:
    removed.is(account)
    list.isEmpty()

    when:
    list.add(account)
    def result = list.remove(account)

    then:
    result
    list.isEmpty()
  }

  def "contains and containsAll"() {
    given:
    def account = new TestAccount(id: "acct1")
    def other = new TestAccount(id: "acct2")
    def list = new ModelList<TestAccount>()
    list.add(account)

    expect:
    list.contains(account)
    list.containsAll([account])
    !list.contains(other)
  }

  def "addAll, removeAll, retainAll, clear"() {
    given:
    def a1 = new TestAccount(id: "a1")
    def a2 = new TestAccount(id: "a2")
    def list = new ModelList<TestAccount>()

    when:
    list.addAll([a1, a2])

    then:
    list.size() == 2

    when:
    list.removeAll([a1])

    then:
    list.size() == 1
    list.contains(a2)

    when:
    list.retainAll([a2])

    then:
    list.size() == 1

    when:
    list.clear()

    then:
    list.isEmpty()
  }

  def "set, indexOf, lastIndexOf"() {
    given:
    def a1 = new TestAccount()
    def a2 = new TestAccount()
    def list = new ModelList<TestAccount>()
    list.add(a1)
    list.add(a1)

    expect:
    list.indexOf(a1) == 0
    list.lastIndexOf(a1) == 1

    when:
    list.set(0, a2)

    then:
    list.get(0).is(a2)
  }

  def "add at index and subList"() {
    given:
    def a1 = new TestAccount()
    def a2 = new TestAccount()
    def list = new ModelList<TestAccount>()
    list.add(a1)

    when:
    list.add(0, a2)

    then:
    list.get(0).is(a2)
    list.get(1).is(a1)

    expect:
    list.subList(0, 1).size() == 1
  }

  def "toArray"() {
    given:
    def list = new ModelList<TestAccount>()
    list.add(new TestAccount())

    expect:
    list.toArray().length == 1
    list.toArray(new TestAccount[0]).length == 1
  }

  def "iterator and listIterator"() {
    given:
    def list = new ModelList<TestAccount>()
    list.add(new TestAccount())

    expect:
    list.iterator().hasNext()
    list.listIterator().hasNext()
    list.listIterator(0).hasNext()
  }

  def "wrapped marks list as wrapped and returns self"() {
    given:
    def list = new ModelList<TestAccount>()

    when:
    def result = list.wrapped()

    then:
    list.getWrapped()
    result.is(list)
  }

  def "ofClass returns ModelList class"() {
    expect:
    ModelList.ofClass(TestAccount) == ModelList
  }

  def "addAll at index"() {
    given:
    def a1 = new TestAccount()
    def a2 = new TestAccount()
    def list = new ModelList<TestAccount>()
    list.add(a1)

    when:
    list.addAll(0, [a2])

    then:
    list.get(0).is(a2)
  }

  def "copyConstructor"() {
    given:
    def account1 = new ChildTestAccount()
    account1.setId("account1234")
    ChildTestAccount account2 = new ChildTestAccount()
    account2.setId("account5678")
    ModelList<ChildTestAccount> childAccountList = new ModelList<ChildTestAccount>()
    childAccountList.add(account1)
    childAccountList.add(account2)

    when:
    ModelList<TestAccount> accounts = new ModelList<TestAccount>(childAccountList)

    then:
    childAccountList.size() == accounts.size()
    "account1234" == accounts.get(0).getId()
    "account5678" == accounts.get(1).getId()
  }
}
