package com.mx.path.gateway.behavior

import com.google.gson.GsonBuilder
import com.mx.accessors.API
import com.mx.accessors.AccessorConfiguration
import com.mx.accessors.AccessorResponse
import com.mx.accessors.BaseAccessor
import com.mx.models.account.Account
import com.mx.path.gateway.configuration.AccessorDescriber

import spock.lang.Specification

class AccessorDescriberTest extends Specification {
  def subject

  @API(description = "test", notes = "notes", specificationUrl = "http://google.com")
  class AccessorImplBase extends BaseAccessor {
    AccessorImplBase(AccessorConfiguration configuration) {
      super(configuration)
    }

    @API(description = "test", notes = "notes", specificationUrl = "http://google.com")
    public AccessorResponse<Account> get(String id) {
      return null
    }
  }

  class AccessorImpl extends AccessorImplBase {
    AccessorImpl(AccessorConfiguration configuration) {
      super(configuration)
    }

    @API(description = "test", notes = "notes", specificationUrl = "http://google.com")
    @Override
    public final AccessorResponse<Account> get(String id) {
      return null
    }
  }

  def setup() {
    subject = new AccessorDescriber()
  }

  def "describe"() {
    given:
    def configuration = AccessorConfiguration.builder().configuration("key", "value").build()
    def accessor = new AccessorImpl(configuration)

    when:
    def description = subject.describe(accessor)

    then:
    description != null
  }

  def "describeDeep"() {
    given:
    def configuration = AccessorConfiguration.builder().configuration("key", "value").build()
    def accessor = new AccessorImpl(configuration)

    when:
    def description = subject.describeDeep(accessor)
    System.out.print(new GsonBuilder().setPrettyPrinting().create().toJson(description))

    then:
    !description.isEmpty()
    description.getAsString("class") == "com.mx.path.gateway.behavior.AccessorDescriberTest.AccessorImpl"
    description.getMap("accessors").getMap("status").getAsString("class") == "com.mx.accessors.StatusDefaultAccessor"
  }
}
