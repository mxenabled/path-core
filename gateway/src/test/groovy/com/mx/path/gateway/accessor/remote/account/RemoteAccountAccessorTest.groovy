package com.mx.path.gateway.accessor.remote.account

import static org.mockito.Mockito.*

import java.time.LocalDate

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.common.messaging.MessageError
import com.mx.common.messaging.MessageStatus
import com.mx.common.serialization.LocalDateDeserializer
import com.mx.models.MdxList
import com.mx.models.account.Account
import com.mx.path.api.connect.messaging.MessageHeaders
import com.mx.path.api.connect.messaging.MessageParameters
import com.mx.path.api.connect.messaging.MessageRequest
import com.mx.path.api.connect.messaging.MessageResponse
import com.mx.path.model.context.RequestContext

import spock.lang.Specification

class RemoteAccountAccessorTest extends Specification {
  RemoteAccountAccessor subject
  private static GsonBuilder gsonBuilder = new GsonBuilder();
  private static Gson gson = gsonBuilder.registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder()
  .build()).create();

  def setup() {
    subject = spy(new RemoteAccountAccessor())
    RequestContext.builder().clientId("clientId").build().register()
  }

  def cleanup() {
    RequestContext.clear()
  }

  def "Throws an exception if the request failed"() {
    given:
    def response = new MessageResponse().tap { setStatus(MessageStatus.FAIL) }
    doReturn(response).when(subject).executeRequest(any(String), any(MessageRequest))

    when:
    subject.get("id").getResult()

    then:
    thrown(MessageError)
  }

  def "Deserializes an MdxList<Account> response and builds a valid MessageRequest"() {
    given:
    def response = new MessageResponse().tap {
      setBody(gson.toJson(new MdxList().tap {
        add(new Account().tap { setId("1234") })
      }))
      setStatus(MessageStatus.SUCCESS)
    }
    doReturn(response).when(subject).executeRequest(any(String), any(MessageRequest))

    when:
    def result = subject.list().getResult()

    then:
    verify(subject).executeRequest("clientId", new MessageRequest().tap {
      setMessageHeaders(new MessageHeaders())
      setMessageParameters(new MessageParameters())
      setOperation("list")
    }) || true
    result instanceof MdxList
    result.size() == 1
    result.get(0).getId() == "1234"
  }

  def "Deserializes an Account response and builds a valid MessageRequest (create)"() {
    given:
    def account = new Account().tap { setId("12435") }
    def response = new MessageResponse().tap {
      setBody(gson.toJson(account))
      setStatus(MessageStatus.SUCCESS)
    }
    doReturn(response).when(subject).executeRequest(any(String), any(MessageRequest))

    when:
    def result = subject.create(account).getResult()

    then:
    verify(subject).executeRequest("clientId", new MessageRequest().tap {
      setMessageHeaders(new MessageHeaders())
      setMessageParameters(new MessageParameters())
      setBody(account)
      setOperation("create")
    }) || true
    result instanceof Account
    result.getId() == "12435"
  }

  def "Deserialized an Account response and builds a valid MessageRequest (get)"() {
    given:
    def account = new Account().tap { setId("12435") }
    def response = new MessageResponse().tap {
      setBody(gson.toJson(account))
      setStatus(MessageStatus.SUCCESS)
    }
    doReturn(response).when(subject).executeRequest(any(String), any(MessageRequest))

    when:
    def result = subject.get("12345").getResult()

    then:
    verify(subject).executeRequest("clientId", new MessageRequest().tap {
      setMessageHeaders(new MessageHeaders())
      setMessageParameters(new MessageParameters().tap {
        setParameters(new HashMap<String, String>().tap { put("id", "12345") })
      })
      setOperation("get")
    }) || true
    result instanceof Account
    result.getId() == "12435"
  }
}
