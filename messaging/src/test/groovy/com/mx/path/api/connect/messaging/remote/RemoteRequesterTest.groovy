package com.mx.path.api.connect.messaging.remote

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*

import com.google.gson.Gson
import com.mx.common.messaging.MessageBroker
import com.mx.common.messaging.MessageError
import com.mx.common.messaging.MessageStatus
import com.mx.models.account.Account
import com.mx.path.api.connect.messaging.MessageEvent
import com.mx.path.api.connect.messaging.MessageHeaders
import com.mx.path.api.connect.messaging.MessageParameters
import com.mx.path.api.connect.messaging.MessageRequest
import com.mx.path.api.connect.messaging.MessageResponse
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.Session
import com.mx.path.model.context.facility.Facilities
import com.mx.path.model.context.store.SessionRepository
import com.mx.testing.RemoteRequesterImpl
import com.mx.testing.TestUtils

import spock.lang.Specification

class RemoteRequesterTest extends Specification {

  Gson gson
  RemoteRequester subject
  SessionRepository sessionRepository

  def setup() {
    TestUtils.startFakedSession()
    sessionRepository = spy(Session.getRepositorySupplier().get())
    Session.setRepositorySupplier({ -> sessionRepository })
    gson = new Gson()
    subject = new RemoteRequesterImpl()
    RequestContext.builder().clientId("client1").userId("user1").build().register()
  }

  def cleanup() {
    TestUtils.endFakedSession()
    RequestContext.clear()
    Facilities.reset()
  }

  def "Request interacts with messageBroker"() {
    given:
    def accounts = new ArrayList<Account>().tap {
      add new Account().tap {
        setId "accountId"
        setType "Checking"
      }
      add new Account().tap {
        setId "accountId2"
        setType "Loan"
      }
    }

    def accountsJson = gson.toJson(accounts)

    MessageRequest payload = MessageRequest.builder()
        .messageHeaders(new MessageHeaders().tap { setSessionId("153df0bc-5dfb-48e0-b366-6fe062fdf47d") }
        )
        .operation("list")
        .build()

    MessageResponse response = MessageResponse.builder()
        .body(accountsJson)
        .status(MessageStatus.SUCCESS)
        .build()

    def messageBroker = mock(MessageBroker)
    def channel = "path.request.afcu.Account.list"

    when(messageBroker.request(any(), any())).thenReturn(response.toJson())
    Facilities.MESSAGE_BROKERS.put("afcu", messageBroker)

    when:
    response = subject.request("afcu", payload)

    then:
    response.getBody() == accountsJson
    verify(messageBroker).request(channel, payload.toJson()) || true
  }

  def "send interacts with messageBroker"() {
    given:
    MessageEvent payload = MessageEvent.builder()
        .body("hi")
        .event("changed")
        .build()

    def messageBroker = mock(MessageBroker)
    def channel = "path.event.afcu.Account.changed"
    Facilities.MESSAGE_BROKERS.put("afcu", messageBroker)

    when:
    subject.send("afcu", payload)

    then:
    verify(messageBroker).publish(channel, payload.toJson()) || true
  }

  def "saves and reloads session"() {
    given:
    Session.createSession()

    MessageRequest payload = MessageRequest.builder()
        .body("hi")
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .build()

    def messageBroker = mock(MessageBroker)
    def channel = "path.event.afcu.Account.changed"
    Facilities.MESSAGE_BROKERS.put("afcu", messageBroker)

    when:
    subject.executeRequest("afcu", payload)

    then:
    verify(sessionRepository).save(any()) || true
    verify(sessionRepository).load(any()) || true
  }


  def "fails if no message broker has been configured for the client"() {
    given:
    Session.createSession()

    MessageRequest payload = MessageRequest.builder()
        .body("hi")
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .build()

    when: "a request is executed"
    def result = subject.executeRequest("afcu", payload)

    then:
    result.status == MessageStatus.DISABLED

    when: "an event is published"
    subject.send("afcu", MessageEvent.builder().build())

    then:
    def e = thrown(MessageError)
    e.messageStatus == MessageStatus.DISABLED
  }

  def "withSession forwards relevant request headers"() {
    given:
    Session.current().setId("id")
    Session.current().setUserId("userId")

    RequestContext.builder()
        .originatingIP("x-forwarded-for")
        .sessionTraceId("session-trace-id")
        .feature("feature")
        .deviceTraceId("device-trace-id")
        .build()
        .register()

    def headers = MessageHeaders.builder().build()

    def message = MessageRequest.builder()
        .messageHeaders(headers)
        .messageParameters(MessageParameters.builder().build())
        .build()

    when:
    def response = subject.withSession(message, { MessageRequest request ->  MessageResponse.builder().build() })

    then:
    response != null
    headers.get("x-forwarded-for") == "x-forwarded-for"
    headers.get("session-trace-id") == "session-trace-id"
    headers.get("feature") == "feature"
    headers.get("device-trace-id") == "device-trace-id"
  }
}
