package com.mx.path.api.connect.http

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify

import com.mx.path.connect.http.HttpAccessorConnection
import com.mx.path.connect.http.HttpRequest
import com.mx.path.connect.http.HttpResponse
import com.mx.path.core.common.request.Feature
import com.mx.path.core.context.RequestContext

import spock.lang.Rollup
import spock.lang.Specification

import testing.connect.RequestFilterStub

class HttpAccessorConnectionTest extends Specification {

  def setup() {
  }

  def cleanup() {
    RequestContext.clear()
  }

  def "builds request"() {
    given:
    def subject = new HttpAccessorConnection()
    subject.setBaseUrl("http://localhost:9999")
    subject.setCertificateAlias("secureMe")

    when:
    def request = subject.request("path")

    then:
    request.getUri() == "http://localhost:9999/path"
    request.getConnectionSettings().getCertificateAlias() == "secureMe"
    request.getFeature() == null
  }

  def "builds request with feature"() {
    given:
    RequestContext.builder().feature("accounts").build().register()

    def subject = new HttpAccessorConnection()
    subject.setBaseUrl("http://localhost:9999")
    subject.setCertificateAlias("secureMe")

    when:
    def request = subject.request("path")

    then:
    request.getUri() == "http://localhost:9999/path"
    request.getConnectionSettings().getCertificateAlias() == "secureMe"
    request.getFeature() == Feature.ACCOUNTS
  }

  @Rollup
  def "builds request with any feature"() {
    given:
    RequestContext.builder().feature(name).build().register()

    def subject = new HttpAccessorConnection()
    subject.setBaseUrl("http://localhost:9999")
    subject.setCertificateAlias("secureMe")

    when:
    def request = subject.request("path")

    then:
    request.getFeature() == enumeration

    where:
    enumeration | name
    Feature.ACCOUNTS  | "accounts"
    Feature.ACH_TRANSFERS  | "ach_transfers"
    Feature.AUTHORIZATIONS  | "authorizations"
    Feature.CHECK_IMAGES  | "check_images"
    Feature.CREDIT_REPORTS  | "credit_reports"
    Feature.CROSS_ACCOUNT_TRANSFERS  | "cross_account_transfers"
    Feature.DOCUMENTS  | "documents"
    Feature.IDENTITY  | "identity"
    Feature.LOCATION  | "location"
    Feature.MANAGED_CARDS  | "managed_cards"
    Feature.ORIGINATIONS  | "originations"
    Feature.PAYMENTS  | "payments"
    Feature.PAYOUTS  | "payouts"
    Feature.PRODUCTS  | "products"
    Feature.PROFILES  | "profiles"
    Feature.REMOTE_DEPOSITS  | "remote_deposits"
    Feature.STATUS  | "status"
    Feature.TRANSACTIONS  | "transactions"
    Feature.TRANSFERS  | "transfers"
  }

  def "delete"() {
    given:
    def subject = spy(new HttpAccessorConnection())
    def fakeRequest = spy(new HttpRequest(new RequestFilterStub()))
    def fakeResponse = new HttpResponse(fakeRequest)

    doReturn(fakeRequest).when(subject).request("tests")
    doReturn(fakeResponse).when(fakeRequest).get()

    when:
    subject.delete("tests")

    then:
    verify(fakeRequest).delete() || true
  }

  def "get"() {
    given:
    def subject = spy(new HttpAccessorConnection())
    def fakeRequest = spy(new HttpRequest(new RequestFilterStub()))
    def fakeResponse = new HttpResponse(fakeRequest)

    doReturn(fakeRequest).when(subject).request("tests")
    doReturn(fakeResponse).when(fakeRequest).get()

    when:
    subject.get("tests")

    then:
    verify(fakeRequest).get() || true
  }

  def "post"() {
    given:
    def subject = spy(new HttpAccessorConnection())
    def fakeRequest = spy(new HttpRequest(new RequestFilterStub()))
    def fakeResponse = new HttpResponse(fakeRequest)
    def body = "body"

    doReturn(fakeRequest).when(subject).request("tests")
    doReturn(fakeResponse).when(fakeRequest).execute()

    when:
    subject.post("tests", body)

    then:
    verify(fakeRequest).post() || true
  }

  def "put"() {
    given:
    def subject = spy(new HttpAccessorConnection())
    def fakeRequest = spy(new HttpRequest(new RequestFilterStub()))
    def fakeResponse = new HttpResponse(fakeRequest)
    def body = "body"

    doReturn(fakeRequest).when(subject).request("tests")
    doReturn(fakeResponse).when(fakeRequest).execute()

    when:
    subject.put("tests", body)

    then:
    verify(fakeRequest).put() || true
  }

  def "patch"() {
    given:
    def subject = spy(new HttpAccessorConnection())
    def fakeRequest = spy(new HttpRequest(new RequestFilterStub()))
    def fakeResponse = new HttpResponse(fakeRequest)
    def body = "body"

    doReturn(fakeRequest).when(subject).request("tests")
    doReturn(fakeResponse).when(fakeRequest).execute()

    when:
    subject.patch("tests", body)

    then:
    verify(fakeRequest).patch() || true
  }
}
