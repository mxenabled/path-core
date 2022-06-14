package com.mx.path.api.lib.realtime

import com.google.gson.Gson
import com.mx.common.http.HttpStatus
import com.mx.path.api.lib.realtime.models.MdxAccount
import com.mx.path.gateway.net.Response
import com.mx.path.gateway.util.MdxApiException

import spock.lang.Specification

class AccountCreateRequestTest extends Specification {
  Gson gson = new Gson()
  def request

  def setup() {
    def account = new MdxAccount()
    account.setName("name")
    account.setType("checking")
    account.setBalance(100.11)
    request = new AccountCreateRequest("base_url", "client_id", "api_key", "user_id", "member_id", account)
  }

  def "Request initialization test"() {

    expect:
    request.getFeatureName() == "id"
    request.getBaseUrl() == "base_url"
    request.getPath() == "/client_id/users/user_id/members/member_id/accounts.json"
    request.getMethod() == "POST"
    request.getHeaders().get("MD-API-KEY") == "api_key"
    request.getHeaders().get("Content-Type") == "application/vnd.moneydesktop.mdx.v5+json"
    request.getHeaders().get("Accept") == "application/vnd.moneydesktop.mdx.v5+json"
    request.getBody() != ""
    def account = gson.fromJson((String)request.getBody(), AccountCreateRequest.MdxAccountWrapper)
    account.getAccount().getBalance() == 100.11
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
    e.getMessage().contains("Error checking/creating Mdx account")
  }
}
