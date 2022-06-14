package com.mx.path.gateway.remote.account

import static org.mockito.Mockito.*

import com.mx.accessors.AccessorResponse
import com.mx.common.collections.ObjectMap
import com.mx.common.messaging.MessageError
import com.mx.common.messaging.MessageStatus
import com.mx.models.MdxList
import com.mx.models.account.Account
import com.mx.path.api.connect.messaging.MessageRequest
import com.mx.path.gateway.api.account.AccountGateway

import spock.lang.Specification

class RemoteAccountGatewayTest extends Specification {
  RemoteAccountGateway subject
  AccountGateway accountGateway
  ObjectMap configurations

  def setup() {
    accountGateway = mock(AccountGateway)
    configurations = new ObjectMap().tap {
      put("list", new ObjectMap())
      put("create", new ObjectMap().tap { put("requireSession", false) })
    }
    subject = spy(new RemoteAccountGateway("clientId", accountGateway, configurations))
  }

  def "throws an exception if the endpoint has not been configured"() {
    when:
    subject.get("channel", MessageRequest.builder().build())

    then:
    def e = thrown(MessageError)
    e.message == "No responder registered for get"
    e.messageStatus == MessageStatus.NO_RESPONDER
  }

  def "requires the session by default"() {
    given:
    doReturn(new AccessorResponse().withResult(new MdxList())).when(accountGateway).list()
    doNothing().when(subject).requireSession()

    when:
    subject.list("channel", MessageRequest.builder().build())

    then:
    verify(subject).requireSession() || true
    verify(accountGateway).list() || true
  }

  def "can disable the session requirement"() {
    given:
    doReturn(new AccessorResponse().withResult(new Account())).when(accountGateway).create(any(Account))
    doNothing().when(subject).requireSession()

    when:
    subject.create("channel", MessageRequest.builder().body(new Account()).build())

    then:
    verify(subject, never()).requireSession() || true
    verify(accountGateway).create(any(Account)) || true
  }

  def "calls the gateway and returns a response"() {
    def account = new Account().tap { setId("1") }
    doReturn(new AccessorResponse().withResult(account)).when(accountGateway).create(any(Account))

    when:
    def result = subject.create("channel", MessageRequest.builder().body(account).build())

    then:
    result.getBodyAs(Account).id == "1"
    result.status == MessageStatus.SUCCESS
    verify(accountGateway).create(any(Account)) || true
  }
}
