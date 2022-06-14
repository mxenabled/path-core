package com.mx.path.api.connect.messaging.remote.models

import com.mx.models.account.Account

import spock.lang.Specification

class RemoteAccountTest extends Specification {

  def memberNumber = "541231"
  def accountMICRNumber = "micro_account_number"

  def "copies account"() {
    given:
    def account = new Account().tap {
      setId("account123")
      setAccountNumber("accountNumber")
    }

    when:
    def remoteAccount = new RemoteAccount(account)
    remoteAccount.setMemberNumber(memberNumber)
    remoteAccount.setAccountMICRNumber(accountMICRNumber)

    then:
    remoteAccount.getId() == account.getId()
    remoteAccount.getAccountNumber() == account.getAccountNumber()
    remoteAccount.getMemberNumber() == memberNumber
    remoteAccount.getAccountMICRNumber() == accountMICRNumber
  }
}
