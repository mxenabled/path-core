package com.mx.path.api.connect.messaging

import com.google.gson.GsonBuilder
import com.mx.common.messaging.MessageError
import com.mx.common.messaging.MessageStatus

import spock.lang.Specification

class MessageErrorThrowableSerializerTest extends Specification {

  def "test MessageErrorThrowableSerializer"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(Throwable.class, new MessageErrorThrowableSerializer()).create()
    Throwable exception = new MessageError("Something bad happened", MessageStatus.DISABLED, null)
    MessageResponse response = MessageResponse.builder().body("hi").exception(exception).build()

    when:
    def serialized = gson.toJson(response)
    System.out.println(serialized)
    def deserialized = gson.fromJson(serialized, MessageResponse.class)

    then:
    deserialized.getException().getClass() == MessageError
    ((MessageError) deserialized.getException()).messageStatus == MessageStatus.DISABLED
  }
}
