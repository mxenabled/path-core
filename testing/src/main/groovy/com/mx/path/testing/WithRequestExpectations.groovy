package com.mx.path.testing

import com.mx.path.core.common.accessor.AccessorConnectionBase
import com.mx.path.core.common.connect.Request
import com.mx.path.testing.request.RequestExpectation
import com.mx.path.testing.request.RequestExpectations
import com.mx.path.testing.request.RequestMatcher

/**
 * Wired:
 *   after each: validate expectations, clean out expectations for next test
 * Available:
 *   setupConnectionMocking: usually not needed, can be invoked anywhere you need to connection expectations reset
 *   connectionMock: used to create a testable connection
 *
 * Usage:
 * <pre>
 *   class ConnectyTest extends Specification implements WithRequestExpectations {
 *
 *     def setup() {
 *       // optional
 *       setupConnectionMocking()
 *
 *       // create a testable connection
 *       def connection = setupConnection(new BankConnection(null))
 *       subject = BankAPI(connection)
 *     }
 *
 *     def "test a connection method"() {
 *       given:
 *       expectConnection(withPath("accounts").withMethod("GET")).andRespond({request, response ->
 *         response.setStatus(HTTPStatus.OK)
 *         response.setBody("{'accounts':[]}")
 *       })
 *
 *       when:
 *       def result = connection.getAccounts()
 *
 *       then:
 *       result.size() == 0
 *     }
 *
 *   }
 * </pre>
 */
trait WithRequestExpectations extends WithMockery {

  RequestExpectation allowConnection(RequestMatcher requestMatcher) {
    stubConnection(requestMatcher)
  }

  def cleanupRequestExpectations() {
    RequestExpectations.verifyConnectionExpectations()
    RequestExpectations.reset()
  }

  RequestMatcher exactly(Request request) {
    RequestMatcher.Fluent.exactly(request)
  }

  RequestExpectation expectConnection(RequestMatcher requestMatcher) {
    RequestExpectations.Fluent.expectRequest(requestMatcher)
  }

  Request request() {
    RequestExpectations.request()
  }

  Request request(RequestMatcher requestMatcher) {
    RequestExpectations.request(requestMatcher)
  }

  List<Request> requests() {
    RequestExpectations.requests()
  }

  List<Request> requests(RequestMatcher requestMatcher) {
    RequestExpectations.requests(requestMatcher)
  }

  def setupConnectionMocking() {
    RequestExpectations.reset()
  }

  AccessorConnectionBase setupConnection(AccessorConnectionBase connection) {
    return RequestExpectations.Fluent.setupConnection(connection)
  }

  RequestExpectation stubConnection(RequestMatcher requestMatcher) {
    RequestExpectations.Fluent.stubRequest(requestMatcher)
  }

  RequestMatcher withMethod(String method) {
    RequestMatcher.Fluent.withMethod(method)
  }

  RequestMatcher withPath(String path) {
    RequestMatcher.Fluent.withPath(path)
  }
}
