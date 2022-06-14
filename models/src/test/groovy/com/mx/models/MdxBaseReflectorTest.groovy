package com.mx.models

import com.mx.models.account.Account
import com.mx.models.payment.PayeeCategory
import com.mx.models.transfer.Transfer

import spock.lang.Specification

class MdxBaseReflectorTest extends Specification {

  MdxBaseReflector subject

  def setup() {
    subject = new MdxBaseReflector()
  }

  def "getId returns result of obj.getId"() {
    given:
    def account = new Account()
    account.setId("account1")

    when:
    def result = subject.getId(account)

    then:
    result == "account1"
  }

  def "getId throws exception when obj.getId not found"() {
    given:
    def payeeCategory = new PayeeCategory()
    payeeCategory.setName("PERSON")

    when:
    subject.getId(payeeCategory)

    then:
    def e = thrown(RuntimeException)
    e.getMessage() == "Unable to reflect getId method from model " + PayeeCategory.class.getCanonicalName()
  }

  def "getRelations"() {
    given:
    def transfer = new Transfer()
    transfer.setId("transfer1")
    transfer.setFromAccountId("fromAccount1")
    transfer.setToAccountId("toAccount1")

    when:
    def result = subject.getRelationDefs(Transfer.class, Account.class)

    then:
    result.size() == 2
    result == subject.getRelationDefs(Transfer.class, Account.class)
  }

  def "getRelationValues"() {
    given:
    def transfer = new Transfer()
    transfer.setId("transfer1")
    transfer.setFromAccountId("fromAccount1")
    transfer.setToAccountId("toAccount1")

    when:
    def result = subject.getRelationIds(transfer, Account.class)

    then:
    result.size() == 2
    result.contains("fromAccount1")
    result.contains("toAccount1")

    when: "null relation id"
    transfer.setFromAccountId(null)
    result = subject.getRelationIds(transfer, Account.class)

    then:
    result.size() == 1
    result.contains("toAccount1")
  }
}
