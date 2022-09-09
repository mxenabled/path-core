package com.mx.path.gateway.behavior

import lombok.Data
import lombok.Getter
import lombok.Setter

import com.google.gson.GsonBuilder
import com.mx.accessors.API
import com.mx.accessors.Accessor
import com.mx.accessors.AccessorConfiguration
import com.mx.accessors.AccessorException
import com.mx.accessors.AccessorResponse
import com.mx.accessors.AccessorResponseStatus
import com.mx.common.gateway.GatewayAPI
import com.mx.path.gateway.configuration.AccessorDescriber
import com.mx.testing.AccountAccessorImpl
import com.mx.testing.accessors.AccountBaseAccessor
import com.mx.testing.accessors.BaseAccessor
import com.mx.testing.model.Account

import spock.lang.Specification

class AccessorDescriberTest extends Specification {
  AccessorDescriber subject

  @API(description = "test", notes = "notes", specificationUrl = "http://google.com")
  class AccessorImplBase extends BaseAccessor {
    @GatewayAPI
    private AccountBaseAccessor accounts;

    AccessorImplBase(AccessorConfiguration configuration) {
      super(configuration)
    }

    @API(description = "test", notes = "notes", specificationUrl = "http://google.com")
    public AccessorResponse<Account> get(String id) {
      return null
    }

    public void setAccounts(AccountBaseAccessor accounts) {
      this.accounts = accounts;
    }

    @API
    public final AccountBaseAccessor accounts() {
      if (accounts!= null) {
        return accounts;
      }

      throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
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
    System.out.print(new GsonBuilder().setPrettyPrinting().create().toJson(description))

    then:
    verifyAll(description) {
      !isEmpty()
      getAsString("class") == "com.mx.path.gateway.behavior.AccessorDescriberTest.AccessorImpl"
      getMap("configurations").get("key") == "value"
    }
  }

  def "describeDeep"() {
    given:
    def configuration = AccessorConfiguration.builder().configuration("key", "value").build()
    def accessor = new AccessorImpl(configuration).tap {
      accounts = new AccountAccessorImpl(configuration)
    }

    when:
    def description = subject.describeDeep(accessor)
    System.out.print(new GsonBuilder().setPrettyPrinting().create().toJson(description))

    then:
    verifyAll (description) {
      !isEmpty()
      getAsString("class") == "com.mx.path.gateway.behavior.AccessorDescriberTest.AccessorImpl"
      getMap("accessors").getMap("accounts").getAsString("class") == "com.mx.testing.AccountAccessorImpl"
      getMap("accessors").getMap("accounts").getMap("accessors").getMap("transactions").getAsString("class") == "com.mx.testing.TransactionAccessorImpl"
    }
  }
}
