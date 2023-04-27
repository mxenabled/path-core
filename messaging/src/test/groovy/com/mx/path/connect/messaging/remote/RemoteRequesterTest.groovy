package com.mx.path.connect.messaging.remote

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*

import java.time.LocalDate

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.path.connect.messaging.MessageEvent
import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageParameters
import com.mx.path.connect.messaging.MessageRequest
import com.mx.path.connect.messaging.MessageResponse
import com.mx.path.connect.messaging.remote.RemoteRequester
import com.mx.path.core.common.messaging.MessageBroker
import com.mx.path.core.common.messaging.MessageError
import com.mx.path.core.common.messaging.MessageStatus
import com.mx.path.core.common.serialization.LocalDateDeserializer
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.Session
import com.mx.path.core.context.facility.Facilities
import com.mx.path.core.context.store.SessionRepository
import com.mx.testing.RemoteAccount
import com.mx.testing.RemoteRequesterImpl
import com.mx.testing.TestUtils

import spock.lang.Specification

class RemoteRequesterTest extends Specification {

  GsonBuilder gsonBuilder = new GsonBuilder();
  Gson gson = gsonBuilder.registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder()
  .build()).create();

  RemoteRequester subject
  SessionRepository sessionRepository

  def setup() {
    TestUtils.startFakedSession()
    sessionRepository = spy(Session.getRepositorySupplier().get())
    Session.setRepositorySupplier({ -> sessionRepository })
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
    def accounts = new ArrayList<RemoteAccount>().tap {
      add new RemoteAccount().tap {
        setId "accountId"
        setType "Checking"
      }
      add new RemoteAccount().tap {
        setId "accountId2"
        setType "Loan"
      }
    }

    def accountsJson = gson.toJson(accounts)

    MessageRequest payload = MessageRequest.builder()
        .messageHeaders(new MessageHeaders().tap {
          setSessionId("153df0bc-5dfb-48e0-b366-6fe062fdf47d")
        }
        )
        .operation("list")
        .build()

    MessageResponse response = MessageResponse.builder()
        .body(accountsJson)
        .status(MessageStatus.SUCCESS)
        .build()

    def messageBroker = mock(MessageBroker)
    def channel = "path.request.client.RemoteAccount.list"

    when(messageBroker.request(any(), any())).thenReturn(response.toJson())
    Facilities.MESSAGE_BROKERS.put("client", messageBroker)

    when:
    response = subject.request("client", payload)

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
    def channel = "path.event.client.RemoteAccount.changed"
    Facilities.MESSAGE_BROKERS.put("client", messageBroker)

    when:
    subject.send("client", payload)

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
    def channel = "path.event.client.Account.changed"
    Facilities.MESSAGE_BROKERS.put("client", messageBroker)

    when:
    subject.executeRequest("client", payload)

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
    def result = subject.executeRequest("client", payload)

    then:
    result.status == MessageStatus.DISABLED

    when: "an event is published"
    subject.send("client", MessageEvent.builder().build())

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
