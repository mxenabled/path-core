package com.mx.path.api.connect.http

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import org.mockito.Mockito

import spock.lang.Specification
import testing.connect.RequestFilterStub

class HttpAccessorConnectionTest extends Specification {

  def setup() {
  }

  def "builds request"() {
    given:
    def subject = new HttpAccessorConnection()
    subject.setBaseUrl("http://localhost:9999")
    subject.setCertificateAlias("secureMe")

    when:
    def request = subject.request("path");

    then:
    request.getUri() == "http://localhost:9999/path"
    request.getConnectionSettings().getCertificateAlias() == "secureMe"
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
