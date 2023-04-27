package com.mx.path.connect.messaging

import com.google.gson.GsonBuilder
import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.messaging.MessageError
import com.mx.path.core.common.messaging.MessageStatus
import com.mx.path.core.common.messaging.RemoteException

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
    deserialized.exception.class == MessageError
    ((MessageError) deserialized.exception).messageStatus == MessageStatus.DISABLED
  }

  def "test MessageErrorThrowableSerializer unknown cause"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(Throwable.class, new MessageErrorThrowableSerializer()).create()
    def responseJson = "{\"status\":\"FAIL\",\"exception\":{\"headers\":{},\"message\":\"The credentials provided were invalid.\",\"report\":true,\"status\":\"USER_ERROR\",\"userMessage\":\"The credentials provided were invalid.\",\"detailMessage\":\"The credentials provided were invalid.\",\"stackTrace\":[],\"suppressedExceptions\":[],\"_type\":\"anUnknownExceptionType\"}}"
    MessageResponse response = MessageResponse.fromJson(responseJson)

    when:
    def serialized = gson.toJson(response)
    def deserialized = gson.fromJson(serialized, MessageResponse.class)

    then:
    deserialized.status == MessageStatus.FAIL
    deserialized.exception.class == RemoteException
    ((RemoteException) deserialized.exception).message == "The credentials provided were invalid."
    ((RemoteException) deserialized.exception).userMessage == "The credentials provided were invalid."
    ((RemoteException) deserialized.exception).shouldReport()
    ((RemoteException) deserialized.exception).status == PathResponseStatus.USER_ERROR
    ((RemoteException) deserialized.exception).originalType == "anUnknownExceptionType"
  }
}
