package com.mx.common.models

import com.mx.testing.Account

import spock.lang.Specification

class MdxListTest extends Specification {

  static class ChildAccount extends Account {}

  def "copyConstructor"() {
    given:
    def account1 = new ChildAccount()
    account1.setId("account1234")
    ChildAccount account2 = new ChildAccount()
    account2.setId("account5678")
    MdxList<ChildAccount> childAccountList = new MdxList<ChildAccount>()
    childAccountList.add(account1)
    childAccountList.add(account2)

    when:
    MdxList<Account> accounts = new MdxList<Account>(childAccountList)

    then:
    childAccountList.size() == accounts.size()
    "account1234" == accounts.get(0).getId()
    "account5678" == accounts.get(1).getId()
  }
}
