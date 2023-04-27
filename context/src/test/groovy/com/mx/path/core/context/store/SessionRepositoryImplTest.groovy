package com.mx.path.core.context.store

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import java.time.LocalDateTime

import com.google.gson.GsonBuilder
import com.mx.path.core.common.serialization.LocalDateTimeDeserializer
import com.mx.path.core.common.store.Store
import com.mx.path.core.context.Session

import org.mockito.Mockito

import spock.lang.Specification

class SessionRepositoryImplTest extends Specification {
  Store store
  SessionRepositoryImpl subject
  Session session

  def setup() {
    store = mock(Store)
    subject = new SessionRepositoryImpl(store)
    Session.createSession()
    session = Session.current()
    session.setId("sessionId")
  }

  def cleanup() {
    Session.clearSession()
  }

  def "delete"() {
    when:
    subject.delete(session)

    then:
    verify(store).delete("sessionId") || true
  }

  def "deleteValue"() {
    when:
    subject.deleteValue(session, "key1")

    then:
    verify(store).delete("sessionId:key1") || true
  }

  def "getValue"() {
    when:
    subject.getValue(session, "key1")

    then:
    verify(store).get("sessionId:key1") || true
  }

  def "load"() {
    given:
    GsonBuilder gsonBuilder = new GsonBuilder();
    def json = gsonBuilder.registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder()
        .build()).create()
        .toJson(session)
    Mockito.doReturn(json).when(store).get("sessionId")

    when:
    def loadedSession = subject.load("sessionId")

    then:
    loadedSession instanceof Session
    loadedSession.getId() == "sessionId"
  }

  def "save"() {
    given:
    GsonBuilder gsonBuilder = new GsonBuilder();
    def json = gsonBuilder.registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder()
        .build()).create()
        .toJson(session)

    when:
    subject.save(session)

    then:
    verify(store).put("sessionId", json, session.getExpiresIn()) || true
  }

  def "saveValue"() {
    when:
    subject.saveValue(session, "key1", "value1")

    then:
    verify(store).put("sessionId:key1", "value1", session.getExpiresIn()) || true
  }

  def "setIfNotExist"() {
    when:
    subject.setIfNotExist(session, "key1", "value1")

    then:
    verify(store).putIfNotExist("sessionId:key1", "value1", session.getExpiresIn()) || true

    when:
    subject.setIfNotExist(session, "key1", "value1", 1234)

    then:
    verify(store).putIfNotExist("sessionId:key1", "value1", 1234) || true
  }
}