package com.mx.path.gateway

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.mx.accessors.AccessorResponse
import com.mx.accessors.BaseAccessor
import com.mx.accessors.id.IdBaseAccessor
import com.mx.models.id.Authentication
import com.mx.path.gateway.api.id.IdGateway
import com.mx.testing.BaseAccessorImpl

import spock.lang.Specification

class IdGatewayTest extends Specification {

  IdBaseAccessor accessor
  BaseAccessor baseAccessor
  IdGateway subject

  def setup() {
    def clientId = "clientId"
    accessor = mock(IdBaseAccessor)
    baseAccessor = new BaseAccessorImpl()
    baseAccessor.setId(accessor)
    subject = IdGateway.builder().baseAccessor(baseAccessor).clientId(clientId).build()
  }

  def "authenticate"() {
    given:
    def response = new AccessorResponse<Authentication>();
    def authentication = new Authentication()

    when:
    when(accessor.authenticate(authentication)).thenReturn(response)

    then:
    subject.authenticate(authentication) == response
  }

  def "authenticateWithUserKey"() {
    given:
    def response = new AccessorResponse<Authentication>();
    def authentication = new Authentication()

    when:
    when(accessor.authenticateWithUserKey(authentication)).thenReturn(response)

    then:
    subject.authenticateWithUserKey(authentication) == response
  }

  def "resumeMfa"() {
    given:
    def response = new AccessorResponse<Authentication>();
    def authentication = new Authentication()

    when:
    when(accessor.resumeMFA(authentication)).thenReturn(response)

    then:
    subject.resumeMFA(authentication) == response
  }

  def "logout"() {
    given:
    def response = new AccessorResponse<>();

    when:
    when(accessor.logout("session-123")).thenReturn(response)
    subject.logout("session-123")

    then:
    verify(accessor).logout("session-123") || true
  }
}
