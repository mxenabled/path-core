package com.mx.path.core.common.model


import com.mx.testing.TestAccount

import spock.lang.Specification

class ModelListTest extends Specification {

  static class ChildTestAccount extends TestAccount {}

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
