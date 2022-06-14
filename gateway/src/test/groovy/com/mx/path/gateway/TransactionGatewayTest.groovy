package com.mx.path.gateway

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import com.mx.accessors.AccessorConfiguration
import com.mx.accessors.AccessorResponse
import com.mx.accessors.BaseAccessor
import com.mx.accessors.account.AccountBaseAccessor
import com.mx.accessors.account.TransactionBaseAccessor
import com.mx.models.MdxList
import com.mx.models.account.Transaction
import com.mx.models.account.TransactionSearchRequest
import com.mx.models.account.TransactionsPage
import com.mx.path.gateway.api.account.TransactionGateway
import com.mx.testing.AccountAccessorImpl
import com.mx.testing.BaseAccessorImpl

import spock.lang.Specification

class TransactionGatewayTest extends Specification {
  TransactionBaseAccessor accessor
  AccountBaseAccessor accountsAccessor
  BaseAccessor baseAccessor
  TransactionGateway subject
  AccessorConfiguration configuration

  def setup() {
    def clientId = "clientId"
    configuration = AccessorConfiguration.builder().clientId(clientId).build()
    accessor = mock(TransactionBaseAccessor)
    accountsAccessor = new AccountAccessorImpl(configuration)

    baseAccessor = new BaseAccessorImpl()
    baseAccessor.setAccounts(accountsAccessor)
    accountsAccessor.setTransactions(accessor)
    subject = TransactionGateway.builder().baseAccessor(baseAccessor).clientId(clientId).build()
  }

  def "recent"() {
    given:
    def response = new AccessorResponse<MdxList<Transaction>>();
    def searchRequest = new TransactionSearchRequest()

    when:
    when(accessor.recent("A-123")).thenReturn(response)

    then:
    subject.recent("A-123") == response
  }

  def "search"() {
    given:
    def response = new AccessorResponse<TransactionsPage>();
    def searchRequest = new TransactionSearchRequest()

    when:
    when(accessor.search("A-123", searchRequest)).thenReturn(response)

    then:
    subject.search("A-123", searchRequest) == response
  }
}
