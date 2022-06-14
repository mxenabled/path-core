package com.mx.path.api.lib.realtime

import com.google.gson.Gson
import com.mx.common.http.HttpStatus
import com.mx.path.gateway.net.Response
import com.mx.path.gateway.util.MdxApiException

import spock.lang.Specification

class MemberCreateRequestTest extends Specification {
  Gson gson
  def request

  def setup() {
    gson = new Gson()
    request = new MemberCreateRequest("base_url", "client_id", "api_key", "user_id", "member_id", "symXchange_id")
  }

  def "Create request with userkey"() {
    given:
    request = new MemberCreateRequest("base_url", "client_id", "api_key", "user_id", "member_id", "symXchange_id")

    expect:
    request.getFeatureName() == "id"
    request.getBaseUrl() == "base_url"
    request.getPath() == "/client_id/users/user_id/members.json"
    request.getMethod() == "POST"
    request.getHeaders().get("MD-API-KEY") == "api_key"
    request.getHeaders().get("Content-Type") == "application/vnd.moneydesktop.mdx.v5+json"
    request.getHeaders().get("Accept") == "application/vnd.moneydesktop.mdx.v5+json"
    def member = gson.fromJson((String)request.getBody(), MemberCreateRequest.MdxMemberWrapper)
    member.getMember().getId() == "member_id"
    member.getMember().getUserKey() == "symXchange_id"
  }

  def "Create request with login and password"() {
    given:
    request = new MemberCreateRequest("base_url", "client_id", "api_key", "user_id", "member_id", "login", "passw0rd".toCharArray())

    expect:
    request.getFeatureName() == "id"
    request.getBaseUrl() == "base_url"
    request.getPath() == "/client_id/users/user_id/members.json"
    request.getMethod() == "POST"
    request.getHeaders().get("MD-API-KEY") == "api_key"
    request.getHeaders().get("Content-Type") == "application/vnd.moneydesktop.mdx.v5+json"
    request.getHeaders().get("Accept") == "application/vnd.moneydesktop.mdx.v5+json"
    def member = gson.fromJson((String)request.getBody(), MemberCreateRequest.MdxMemberWrapper)
    member.getMember().getId() == "member_id"
    member.getMember().getLogin() == "login"
    member.getMember().getPassword() == "passw0rd"
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

  def "test for onComplete callback, 409 -> no exception"() {
    given:
    def response = new Response()
    response.withStatus(HttpStatus.CONFLICT)

    when:
    request.completed(response)

    then:
    noExceptionThrown()
  }

  def "test for onComplete callback, 500 -> exception"() {
    given:
    def response = new Response()
    response.withStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    when:
    request.completed(response)

    then:
    def e = thrown(MdxApiException)
    e.getMessage().contains("Error checking/creating Mdx member")
  }
}
