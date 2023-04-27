package com.mx.path.connect.messaging.remote

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.*

import com.google.gson.Gson
import com.mx.path.connect.messaging.MessageEvent
import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageRequest
import com.mx.path.connect.messaging.MessageResponse
import com.mx.path.connect.messaging.remote.InvalidServiceClassException
import com.mx.path.connect.messaging.remote.RemoteChannel
import com.mx.path.connect.messaging.remote.RemoteService
import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.messaging.EventListener
import com.mx.path.core.common.messaging.MessageBroker
import com.mx.path.core.common.messaging.MessageError
import com.mx.path.core.common.messaging.MessageResponder
import com.mx.path.core.common.messaging.MessageStatus
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.Session
import com.mx.path.core.context.SessionRepositorySupplier
import com.mx.path.core.context.facility.Facilities
import com.mx.path.core.context.store.SessionRepository
import com.mx.path.core.context.tracing.CustomTracer
import com.mx.testing.RemoteAccount
import com.mx.testing.RemoteServiceValid
import com.mx.testing.RemoteServiceWithInvalid
import com.mx.testing.RemoteServiceWithObserver
import com.mx.testing.TestUtils
import com.mx.testing.fakes.FakeStore

import io.opentracing.mock.MockTracer

import spock.lang.Specification

class RemoteServiceTest extends Specification {
  private MessageBroker messageBroker
  private RemoteService<RemoteAccount> subject
  private SessionRepository sessionRepository

  def setup() {
    TestUtils.startFakedSession()
    sessionRepository = spy(Session.getRepositorySupplier().get())
    Session.setRepositorySupplier({ -> sessionRepository })
    messageBroker = mock(MessageBroker)

    subject = new RemoteServiceWithObserver("client")
    subject.setMessageBrokerSupplier({ -> messageBroker })

    CustomTracer.setTracer(new MockTracer())
  }

  def cleanup() {
    TestUtils.endFakedSession()
    CustomTracer.setTracer(null)
  }

  def "register messageResponder interacts with messageBroker"() {
    given:
    def messageResponder = mock(MessageResponder)

    when:
    subject.register("path.request.client.accounts.list", messageResponder)

    then:
    verify(messageBroker).registerResponder("path.request.client.accounts.list", messageResponder) || true
  }

  def "register eventListener interacts with messageBroker"() {
    given:
    def eventListener = mock(EventListener)

    when:
    subject.register("path.event.client.RemoteAccount.list", eventListener)

    then:
    verify(messageBroker).registerListener("path.event.client.RemoteAccount.list", eventListener) || true
  }

  def "register registers all annotated listeners and responders"() {
    given:
    subject = new RemoteServiceValid("client")
    subject.setMessageBrokerSupplier({ -> messageBroker })

    when:
    subject.register()

    then:
    verify(messageBroker).registerResponder("path.request.client.RemoteAccount.list", subject) || true
    verify(messageBroker).registerListener("path.event.client.RemoteAccount.changed", subject) || true
  }


  def "registerResponder"() {
    given:
    def subjectNoDispatch = new RemoteServiceWithInvalid("client")
    subjectNoDispatch.setMessageBrokerSupplier({ -> messageBroker })

    when: "valid responder"
    subjectNoDispatch.registerResponder("list")

    then:
    verify(messageBroker).registerResponder("path.request.client.RemoteAccount.list", subjectNoDispatch)

    when: "invalid responder"
    subjectNoDispatch.registerResponder("wrongArgs")

    then:
    def ex = thrown(InvalidServiceClassException)
    ex.message == "Service class (RemoteServiceWithInvalid) does not implement responder for wrongArgs. Check argument types and method name."

    when: "invalid responder return type"
    subjectNoDispatch.registerResponder("invalid")

    then:
    ex = thrown(InvalidServiceClassException)
    ex.message == "Service class (RemoteServiceWithInvalid) responder method (invalid) has invalid return type. Must be MessageResponse."
  }

  def "registerListener"() {
    given:
    def subjectNoDispatch = new RemoteServiceWithInvalid("client")
    subjectNoDispatch.setMessageBrokerSupplier({ -> messageBroker })

    when: "valid responder"
    subjectNoDispatch.registerListener("changed")

    then:
    verify(messageBroker).registerListener("path.event.client.RemoteAccount.changed", subjectNoDispatch)

    when: "invalid responder"
    subjectNoDispatch.registerListener("bad")

    then:
    def ex = thrown(InvalidServiceClassException)
    ex.message == "Service class (RemoteServiceWithInvalid) does not implement listener for bad. Check argument types and method name."
  }

  def "respond invokes message responder"() {
    given:
    def messageResponder = mock(MessageResponder)
    subject.setMockTestObserverMessageResponder(messageResponder)

    when:
    def message = MessageRequest.builder().body("hi").build()
    subject.respond("fake.channel", message.toJson())

    then:
    verify(messageResponder).respond(eq("fake.channel"), eq(message.toJson())) || true
  }

  def "loads the Session using Facilities on dispatch"() {
    given:
    subject = new RemoteServiceValid("client")
    Session.setRepositorySupplier(new SessionRepositorySupplier())
    Facilities.setSessionStore("client", new FakeStore(new ObjectMap()))

    when:
    def message = MessageRequest.builder().body("hi").messageHeaders(
        MessageHeaders
        .builder()
        .header("x-forwarded-for", "x-forwarded-for")
        .header("session-trace-id", "session-trace-id")
        .header("feature", "feature")
        .sessionId("sessionId")
        .build()
        ).build()
    def response = subject.dispatch(RemoteChannel.buildRequestChannel("client", Object.class, message), message)

    then:
    response.status != MessageStatus.FAIL
  }

  def "withSessionContext inflates the RequestContext and cleans it up"() {
    given:
    def message = MessageRequest.builder().body("hi").messageHeaders(
        MessageHeaders
        .builder()
        .header("x-forwarded-for", "x-forwarded-for")
        .header("session-trace-id", "session-trace-id")
        .header("feature", "feature")
        .header("device-trace-id", "device-trace-id")
        .sessionId("sessionId")
        .build()
        ).build()

    def channel = RemoteChannel.builder()
        .clientId("clientId")
        .type(RemoteChannel.ChannelType.REQUEST)
        .build()

    // We have to assert that the RequestContext was inflated correctly in this function,
    // because it gets immediately cleared right after this lambda finishes.
    def asserter = {
      ->
      def requestContext = RequestContext.current()
      assert requestContext.originatingIP == "x-forwarded-for"
      assert requestContext.sessionTraceId == "session-trace-id"
      assert requestContext.feature == "feature"
      assert requestContext.deviceTraceId == "device-trace-id"
    }

    when:
    subject.withSessionContext(channel, message, asserter)

    then:
    RequestContext.current() == null
  }

  def "withSessionContext inflates the RequestContext and cleans it up (null values)"() {
    given:
    def message = MessageRequest.builder().body("hi").messageHeaders(
        MessageHeaders
        .builder()
        .sessionId("sessionId")
        .build()
        ).build()

    def channel = RemoteChannel.builder()
        .clientId("clientId")
        .type(RemoteChannel.ChannelType.REQUEST)
        .build()

    // We have to assert that the RequestContext was inflated correctly in this function,
    // because it gets immediately cleared right after this lambda finishes.
    def asserter = {
      ->
      def requestContext = RequestContext.current()
      assert requestContext.originatingIP == null
      assert requestContext.sessionTraceId == null
      assert requestContext.feature == null
      assert requestContext.deviceTraceId == null
    }

    when:
    subject.withSessionContext(channel, message, asserter)

    then:
    RequestContext.current() == null
  }

  def "respond invokes message responder and handles session"() {
    given:
    Session.createSession()
    Session.current().save()

    def messageResponder = mock(MessageResponder)
    subject.setMockTestObserverMessageResponder(messageResponder)

    when:
    def message = MessageRequest.builder().body("hi").build()
    subject.respond("fake.channel", message.toJson())

    then:
    verify(sessionRepository).save(any()) || true
  }

  def "receive invokes eventListener"() {
    given:
    def eventListener = mock(EventListener)
    subject.setMockTestObserverEventListener(eventListener)
    def message = MessageEvent.builder().body("hi").build()

    when:
    subject.receive("fake.channel", message.toJson())

    then:
    verify(eventListener).receive(any(), any()) || true
  }

  def "receive not implemented"() {
    given:
    def noOpSubject = new RemoteServiceValid();
    def message = MessageEvent.builder().body("hi").build()

    when:
    noOpSubject.receive("path.event.client.RemoteAccount.not_implemented", message.toJson())

    then:
    noExceptionThrown()
  }

  def "respond not implemented"() {
    given:
    def noOpSubject = new RemoteServiceValid();
    def message = MessageRequest.builder().body("hi").build()

    when:
    def responseStr = noOpSubject.respond("path.request.client.RemoteAccount.not_implemented", message.toJson())
    MessageResponse response = new Gson().fromJson(responseStr, MessageResponse)

    then:
    noExceptionThrown()
    response.getStatus() == MessageStatus.NO_RESPONDER
    response.getException() != null
  }

  def "requireSession"() {
    when: "session is present in bad state"
    Session.createSession()
    Session.current().setSessionState(Session.SessionState.AUTHENTICATED)
    subject.requireSession()

    then:
    noExceptionThrown()

    when: "session is present in bad state"
    Session.createSession()
    Session.current().setSessionState(Session.SessionState.CHALLENGED)
    subject.requireSession()

    then:
    thrown(MessageError)

    when: "no session exists"
    Session.clearSession()
    subject.requireSession()

    then:
    def e = thrown(MessageError)
    e.getMessageStatus() == MessageStatus.NOT_AUTHORIZED
  }
}
