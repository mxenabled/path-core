package com.mx.path.gateway.behavior

import com.google.gson.GsonBuilder
import com.mx.path.core.common.accessor.API
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException
import com.mx.path.core.common.gateway.GatewayAPI
import com.mx.path.gateway.accessor.AccessorConfiguration
import com.mx.path.gateway.accessor.AccessorResponse
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

    AccessorImplBase() {}

    @API(description = "test", notes = "notes", specificationUrl = "http://google.com")
    public AccessorResponse<Account> get(String id) {
      return null
    }

    public void setAccounts(AccountBaseAccessor accounts) {
      this.accounts = accounts;
    }

    @API
    public final AccountBaseAccessor accounts() {
      if (accounts != null) {
        return accounts;
      }

      throw new AccessorMethodNotImplementedException();
    }
  }

  class AccessorImpl extends AccessorImplBase {
    AccessorImpl() {}

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
    def accessor = new AccessorImpl()
    accessor.setConfiguration(configuration)

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
    def accessor = new AccessorImpl().tap {
      accounts = new AccountAccessorImpl()
    }

    when:
    def description = subject.describeDeep(accessor)
    System.out.print(new GsonBuilder().setPrettyPrinting().create().toJson(description))

    then:
    verifyAll(description) {
      !isEmpty()
      getAsString("class") == "com.mx.path.gateway.behavior.AccessorDescriberTest.AccessorImpl"
      getMap("accessors").getMap("accounts").getAsString("class") == "com.mx.testing.AccountAccessorImpl"
      getMap("accessors").getMap("accounts").getMap("accessors").getMap("transactions").getAsString("class") == "com.mx.testing.TransactionAccessorImpl"
    }
  }
}
