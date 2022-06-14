package com.mx.path.api.lib.realtime

import com.google.gson.Gson
import com.mx.common.http.HttpStatus
import com.mx.path.gateway.net.Response

import spock.lang.Specification

class MemberGetRequestTest extends Specification {

  Gson gson
  def request

  def setup() {
    gson = new Gson()
    request = new MemberGetRequest("base_url", "client_id", "api_key", "user_id", "member_id")
  }

  def "Get member"() {
    given:
    request = new MemberGetRequest("base_url", "client_id", "api_key", "user_id", "member_id")

    expect:
    request.getFeatureName() == "id"
    request.getBaseUrl() == "base_url"
    request.getPath() == "/client_id/users/user_id/members/member_id.json"
    request.getMethod() == "GET"
    request.getHeaders().get("MD-API-KEY") == "api_key"
    request.getHeaders().get("Accept") == "application/vnd.moneydesktop.mdx.v5+json"
  }

  def "test for onComplete callback, 200 -> no exception"() {
    given:
    def response = new Response()
    response.withStatus(HttpStatus.OK)

    when:
    request.completed(response)

    then:
    noExceptionThrown()
  }
}
