package com.mx.path.model.context.store

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import com.google.gson.Gson
import com.mx.common.store.Store
import com.mx.path.model.context.Session

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
    Mockito.doReturn(new Gson().toJson(session)).when(store).get("sessionId")

    when:
    def loadedSession = subject.load("sessionId")

    then:
    loadedSession instanceof Session
    loadedSession.getId() == "sessionId"
  }

  def "save"() {
    given:
    def json = new Gson().toJson(session)

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

  def "getRedisResponse"() {
    when:
    subject.getRedisResponse()

    then:
    verify(store).status() || true
  }
}