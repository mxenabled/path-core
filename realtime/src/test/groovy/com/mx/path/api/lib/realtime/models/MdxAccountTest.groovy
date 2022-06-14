package com.mx.path.api.lib.realtime.models

import spock.lang.Specification

class MdxAccountTest extends Specification {

  def "test for getter/setter"() {
    given:
    def account = new MdxAccount()

    when:
    account.setAvailableBalance(12000000.3)
    account.setAccountNumber("account_number")
    account.setAvailableCredit(45000000.6)
    account.setBalance(78000000.9)
    account.setApr(0.1)
    account.setApy(0.2)
    account.setCreditLimit(12300000.0)
    account.setCurrencyCode("US DOLLAR")
    account.setDayPaymentIsDue("01-02-2020")
    account.setGuid("guid_1234")
    account.setHasMonthlyTransferLimit(true)
    account.setId("id")
    account.setInterestRate(0.03)
    account.setIsClosed(false)
    account.setIsHidden(true)
    account.setLastPayment(10000000.1)
    account.setLastPaymentAt(12345L)
    account.setLastPaymentOn("02-02-2020")
    account.setMaturesAt(12345L)
    account.setMaturesOn("04-15-2020")
    account.setMemberGuid("guid_5678")
    account.setMemberId("member_id")
    account.setMetadata("metadata")
    account.setMinimumBalance(30000000.0)
    account.setMinimumPayment(10000000.2)
    account.setMonthlyTransferCount(10)
    account.setName("name")
    account.setNickname("nickname")
    account.setOriginalBalance(40000000.80)
    account.setPaymentDueAt(12345678L)
    account.setPaymentDueOn("03-01-2020")
    account.setPayoffBalance(10000000.0)
    account.setPendingBalance(30000000.30)
    account.setRoutingTransitNumber("number")
    account.setStartedAt(789L)
    account.setStartedOn("01-01-2021")
    account.setStatementBalance(30000000.0)
    account.setSubtype("subtype")
    account.setType("type")

    then:
    account.getAvailableBalance() == 12000000.3
    account.getAccountNumber() == "account_number"
    account.getAvailableCredit() == 45000000.6
    account.getBalance() == 78000000.9
    account.getApr() == 0.1
    account.getApy() == 0.2
    account.getCreditLimit() == 12300000.0
    account.getCurrencyCode() == "US DOLLAR"
    account.getDayPaymentIsDue() == "01-02-2020"
    account.getGuid() == "guid_1234"
    account.getHasMonthlyTransferLimit()
    account.getId() == "id"
    account.getInterestRate() == 0.03
    !account.getIsClosed()
    account.getIsHidden()
    account.getLastPayment() == 10000000.1
    account.getLastPaymentAt() == 12345L
    account.getLastPaymentOn() == "02-02-2020"
    account.getMaturesAt() == 12345L
    account.getMaturesOn() == "04-15-2020"
    account.getMemberGuid() == "guid_5678"
    account.getMemberId() == "member_id"
    account.getMetadata() == "metadata"
    account.getMinimumBalance() == 30000000.0
    account.getMinimumPayment() == 10000000.2
    account.getMonthlyTransferCount() == 10
    account.getName() == "name"
    account.getNickname() == "nickname"
    account.getOriginalBalance() == 40000000.80
    account.getPaymentDueAt() == 12345678L
    account.getPaymentDueOn() == "03-01-2020"
    account.getPayoffBalance() == 10000000.0
    account.getPendingBalance() == 30000000.30
    account.getRoutingTransitNumber() == "number"
    account.getStartedAt() == 789L
    account.getStartedOn() == "01-01-2021"
    account.getStatementBalance() == 30000000.0
    account.getSubtype() == "subtype"
    account.getType() == "type"
  }
}
