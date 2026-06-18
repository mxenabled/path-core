package com.mx.path.gateway.accessor

import com.mx.path.core.common.connect.Request
import com.mx.path.core.common.connect.RequestFilter
import com.mx.path.core.common.connect.RequestFilterBase
import com.mx.path.core.common.connect.Response

import spock.lang.Specification

class AccessorConnectionBaseTest extends Specification {

  static class NoOpFilter extends RequestFilterBase {
    boolean executed = false
    @Override
    void execute(Request request, Response response) {
      executed = true
    }
  }

  static class ConcreteConnection extends AccessorConnectionBase<Object> {
    List<RequestFilter> extraFilters = []

    @Override
    List<RequestFilter> connectionRequestFilters() {
      [new NoOpFilter()]
    }

    @Override
    Object request(String path) {
      null
    }

    @Override
    void preprocessFilterChain(List<RequestFilter> filters) {
      filters.addAll(extraFilters)
    }
  }

  def "buildFilterChain constructs a linked filter chain"() {
    given:
    def connection = new ConcreteConnection()
    def baseFilter = new NoOpFilter()
    connection.setBaseRequestFilters([baseFilter])

    when:
    def chain = connection.buildFilterChain()

    then:
    chain != null
    chain.is(baseFilter)
    baseFilter.getNext() instanceof NoOpFilter
  }

  def "buildFilterChain caches result on repeated calls"() {
    given:
    def connection = new ConcreteConnection()

    when:
    def chain1 = connection.buildFilterChain()
    def chain2 = connection.buildFilterChain()

    then:
    chain1.is(chain2)
  }

  def "buildFilterChain calls preprocessFilterChain to allow modification"() {
    given:
    def extraFilter = new NoOpFilter()
    def connection = new ConcreteConnection(extraFilters: [extraFilter])

    when:
    connection.buildFilterChain()

    then:
    noExceptionThrown()
  }

  def "buildFilterChain works with no base filters"() {
    given:
    def connection = new ConcreteConnection()
    connection.setBaseRequestFilters(null)

    when:
    def chain = connection.buildFilterChain()

    then:
    chain instanceof NoOpFilter
  }
}
