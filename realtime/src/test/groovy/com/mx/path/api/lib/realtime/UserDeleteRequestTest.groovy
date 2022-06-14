package com.mx.path.api.lib.realtime

import com.mx.common.http.HttpStatus
import com.mx.path.gateway.net.Response

import spock.lang.Specification

class UserDeleteRequestTest extends Specification {
  def request

  def setup() {
    request = new UserDeleteRequest("base_url", "client_id", "api_key", "user_id")
  }

  def "Request initialization test"() {
    expect:
    request.featureName == "origination"
    request.baseUrl == "base_url"
    request.path == "/client_id/users/user_id.xml"
    request.method == "DELETE"
    request.getHeaders().get("MD-API-KEY") == "api_key"
    request.getHeaders().get("Accept") == "application/vnd.moneydesktop.mdx.v5+json"
  }

  def "test for onComplete callback, 204 -> no exception"() {
    given:
    def response = new Response()
    response.withStatus(HttpStatus.NO_CONTENT)

    when:
    request.completed(response)

    then:
    noExceptionThrown()
  }

  def "test for onComplete callback, 404 -> exception"() {
    given:
    def response = new Response()
    response.withStatus(HttpStatus.NOT_FOUND)

    when:
    request.completed(response)

    then:
    def e = thrown(RuntimeException)
    e.message == "Error deleting Mdx user"
  }
}
