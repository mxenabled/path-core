package com.mx.path.connect.messaging.remote

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.*

import com.mx.path.connect.messaging.MessageResponse
import com.mx.path.core.common.facility.FacilityException
import com.mx.path.core.common.messaging.MessageBroker
import com.mx.path.core.common.messaging.MessageBrokerException
import com.mx.path.core.common.messaging.MessageError
import com.mx.path.core.common.messaging.MessageStatus
import com.mx.path.core.common.model.ModelList
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.facility.Facilities
import com.mx.testing.RemoteAccount

import spock.lang.Specification

class RemoteCRUDModelTest extends Specification {
  class RemoteAccountCrud extends RemoteCRUDModel<RemoteAccount> {
    RemoteAccountCrud(String clientId) {
      super(clientId)
    }
  }

  RemoteAccountCrud subject
  MessageBroker messageBroker
  void setup() {
    messageBroker = mock(MessageBroker)
    subject = new RemoteAccountCrud("client123")
    Facilities.MESSAGE_BROKERS.put("client123", messageBroker)
    RequestContext.builder().clientId("client123").build().register()
  }

  def cleanup() {
    RequestContext.clear()
  }

  def "delete"() {
    when:
    def response = MessageResponse.builder().status(MessageStatus.SUCCESS).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.delete"), any(String))).thenReturn(response.toJson())

    subject.delete("account-123")

    then:
    verify(messageBroker).request(eq("path.request.client123.RemoteAccount.delete"), any(String)) || true
  }

  def "delete with error"() {
    when:
    def response = MessageResponse.builder().status(MessageStatus.NOT_AUTHORIZED).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.delete"), any(String))).thenReturn(response.toJson())

    subject.delete("account-123")

    then:
    def e = thrown(MessageError)
    e.getMessageStatus() == MessageStatus.NOT_AUTHORIZED
  }

  def "get"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")

    when:
    def response = MessageResponse.builder().status(MessageStatus.SUCCESS).body(account).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.get"), any(String))).thenReturn(response.toJson())

    def accountResponse = subject.get("account-123")

    then:
    verify(messageBroker).request(eq("path.request.client123.RemoteAccount.get"), any(String)) || true
    accountResponse.getId() == "account-123"
  }

  def "get with error"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")

    when:
    def response = MessageResponse.builder().status(MessageStatus.NOT_AUTHORIZED).body(account).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.get"), any(String))).thenReturn(response.toJson())

    subject.get("account-123")

    then:
    def e = thrown(MessageError)
    e.getMessageStatus() == MessageStatus.NOT_AUTHORIZED
  }

  def "list"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")

    def accounts = new ModelList<RemoteAccount>(Arrays.asList(account))

    when:
    def response = MessageResponse.builder().status(MessageStatus.SUCCESS).body(accounts).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.list"), any(String))).thenReturn(response.toJson())

    def accountsResponse = subject.list()

    then:
    verify(messageBroker).request(eq("path.request.client123.RemoteAccount.list"), any(String)) || true
    accountsResponse.first().getId() == "account-123"
  }

  def "list with error"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")

    def accounts = new ModelList<RemoteAccount>(Arrays.asList(account))

    when:
    def response = MessageResponse.builder().status(MessageStatus.NOT_AUTHORIZED).body(accounts).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.list"), any(String))).thenReturn(response.toJson())

    subject.list()

    then:
    def e = thrown(MessageError)
    e.getMessageStatus() == MessageStatus.NOT_AUTHORIZED
  }

  def "update"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")
    account.setBalance(12.0)

    when:
    def response = MessageResponse.builder().status(MessageStatus.SUCCESS).body(account).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.update"), any(String))).thenReturn(response.toJson())

    def accountResponse = subject.update(account.getId(), account)

    then:
    verify(messageBroker).request(eq("path.request.client123.RemoteAccount.update"), any(String)) || true
    accountResponse.getId() == "account-123"
  }

  def "update with error"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")
    account.setBalance(12.0)

    when:
    def response = MessageResponse.builder().status(MessageStatus.NOT_AUTHORIZED).body(account).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.update"), any(String))).thenReturn(response.toJson())

    subject.update(account.getId(), account)

    then:
    def e = thrown(MessageError)
    e.getMessageStatus() == MessageStatus.NOT_AUTHORIZED
  }

  def "create"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")
    account.setBalance(12.0)

    when:
    def response = MessageResponse.builder().status(MessageStatus.SUCCESS).body(account).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.create"), any(String))).thenReturn(response.toJson())

    def accountResponse = subject.create(account)

    then:
    verify(messageBroker).request(eq("path.request.client123.RemoteAccount.create"), any(String)) || true
    accountResponse.getId() == "account-123"
  }

  def "create with error"() {
    given:
    def account = new RemoteAccount()
    account.setId("account-123")
    account.setBalance(12.0)

    when:
    def response = MessageResponse.builder().status(MessageStatus.NOT_AUTHORIZED).body(account).build()
    when(messageBroker.request(eq("path.request.client123.RemoteAccount.create"), any(String))).thenReturn(response.toJson())

    subject.create(account)

    then:
    def e = thrown(MessageBrokerException)
  }
}
