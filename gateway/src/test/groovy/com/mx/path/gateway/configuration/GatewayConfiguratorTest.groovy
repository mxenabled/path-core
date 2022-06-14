package com.mx.path.gateway.configuration

import com.mx.common.collections.ObjectMap
import com.mx.path.gateway.accessor.proxy.BaseAccessorProxySingleton
import com.mx.path.gateway.api.Gateway
import com.mx.path.gateway.api.account.AccountGateway
import com.mx.path.gateway.api.account.TransactionGateway
import com.mx.path.gateway.api.id.IdGateway
import com.mx.path.gateway.behavior.GatewayBehavior
import com.mx.path.gateway.remote.account.RemoteAccountGateway
import com.mx.path.gateway.remote.account.RemoteTransactionGateway
import com.mx.path.model.context.facility.Facilities
import com.mx.testing.ChildAccessorAccount
import com.mx.testing.ChildAccessorBase
import com.mx.testing.EncryptionServiceImpl
import com.mx.testing.FaultTolerantExecutorImpl
import com.mx.testing.IdAccessorImpl
import com.mx.testing.MessageBrokerImpl
import com.mx.testing.StoreImpl

import spock.lang.Specification

class GatewayConfiguratorTest extends Specification {

  def "fromJson empty"() {
    given:
    def subject = new GatewayConfigurator();

    when: "empty json"
    def result = subject.buildFromJson("")

    then:
    result.isEmpty()

    when: "null json"
    result = subject.buildFromJson(null)

    then:
    result.isEmpty()
  }

  def "fromJson"() {
    given:
    def subject = new GatewayConfigurator();

    when:
    def file = new File("src/test/resources/gatewayConfig.json")
    def json = String.join("\n", file.readLines("UTF-8"))
    def result = subject.buildFromJson(json)

    then:
    verifyGatewayMap(result)
  }

  def "fromYaml empty"() {
    given:
    def subject = new GatewayConfigurator();

    when: "empty yaml"
    def result = subject.buildFromYaml("")

    then:
    result.isEmpty()

    when: "null yaml"
    result = subject.buildFromYaml(null)

    then:
    result.isEmpty()
  }

  def "fromYaml"() {
    given:
    def subject = new GatewayConfigurator();

    when:
    def file = new File("src/test/resources/gatewayConfig.yaml")
    def yaml = String.join("\n", file.readLines("UTF-8"))
    def result = subject.buildFromYaml(yaml)

    then:
    verifyGatewayMap(result)
  }

  def "fromYaml anchors"() {
    given:
    def subject = new GatewayConfigurator()

    when:
    def file = new File("src/test/resources/gatewayConfigAnchors.yaml")
    def yaml = String.join("\n", file.readLines("UTF-8"))
    def result = subject.buildFromYaml(yaml)

    then:
    verifyGatewayMap(result)
  }

  def "fromYaml definitions"() {
    given:
    def subject = new GatewayConfigurator()

    when:
    def file = new File("src/test/resources/gatewayConfigDefinitions.yaml")
    def yaml = String.join("\n", file.readLines("UTF-8"))
    def result = subject.buildFromYaml(yaml)

    then:
    verifyGatewayMap(result)
  }

  def "verify Gateway BehaviorSequence"(){
    given:
    def subject = new GatewayConfigurator()

    when:
    def file = new File("src/test/resources/gatewayConfigBehaviorSequence.yaml")
    def yaml = String.join("\n", file.readLines("UTF-8"))
    def result = subject.buildFromYaml(yaml)

    then:
    assert result["clientName"].behaviors.size() == 1
    assert result["clientName"].behaviors.get(0).getClass().canonicalName == "com.mx.testing.GatewayBehaviorImpl"
    assert result["clientName"].id().behaviors.size() == 2
    assert result["clientName"].id().behaviors.get(0).getClass().canonicalName == "com.mx.testing.GatewayBehaviorImpl"
    assert result["clientName"].id().behaviors.get(1).getClass().canonicalName =="com.mx.testing.MDXGatewayBehaviorImpl"
  }

  def "build gateway with child accessor annotations"() {
    given:
    def subject = new GatewayConfigurator()
    def objectMap = new ObjectMap()

    def baseAccessorConf = objectMap.createMap("accessor")
    baseAccessorConf.put("class", ChildAccessorBase.class.getCanonicalName())
    baseAccessorConf.put("scope", "singleton")
    baseAccessorConf.put("configurations", new ObjectMap().tap { put("Wakirimasu ka?", "Hai!") })
    baseAccessorConf.put("connections", new ObjectMap().tap {
      put("MyConnection", new ObjectMap().tap {
        put("baseUrl", "https://example.com")
        put("configurations", new ObjectMap().tap { put("Hi", "There") })
      })
    })

    def gatewaysConf = objectMap.createMap("gateways")
    gatewaysConf.createMap("accounts")

    when:
    def result = subject.buildGateway(objectMap, "client1")

    then:
    result.getBaseAccessor().getClass() == BaseAccessorProxySingleton
    ((AccessorProxy) result.accounts().getAccessor()).getAccessorClass() == ChildAccessorAccount
    def configuration = result.accounts().getAccessor().getConfiguration().getConfigurations()
    def connection = result.accounts().getAccessor().getConfiguration().getConnections().getConnection("MyConnection")
    configuration == new ObjectMap().tap { put("Wakirimasu ka?", "Hai!") }
    connection.getBaseUrl() == "https://example.com"
    connection.getConfigurations() == new ObjectMap().tap { put("Hi", "There") }
  }

  def verifyGatewayMap(Map<String, Gateway> map) {
    assert map.size() == 1
    assert map["clientName"].behaviors.size() == 1 // todo: Need to figure out how to keep this from happening. RootBehaviors should only be added to sub-gateways, they make no sense here.
    assert map["clientName"].services.size() == 1
    assert map["clientName"].services.get(0) != null
    assert map["clientName"].baseAccessor != null
    assert map["clientName"].baseAccessor.getClass() == BaseAccessorProxySingleton.class
    assert map["clientName"].baseAccessor.configuration.connections.getConnection("testConnection").skipHostNameVerify
    assert map["clientName"].baseAccessor.configuration.connections.getConnection("testConnection").configurations.getAs(ArrayList, "someMapOfValues") == [1, 2, 3]
    assert map["clientName"].baseAccessor.configuration.configurations.get("key1") == "value1"
    assert map["clientName"].baseAccessor.configuration.configurations.get("key2") == "value2"
    assert map["clientName"].baseAccessor.configuration.connections.getConnection("anotherTestConnection").configurations.get("arbitrary") == "value"

    assert map["clientName"].baseAccessor.id().class == IdAccessorImpl.class
    assert map["clientName"].baseAccessor.id().configuration.configurations.get("key1") == "value1"
    assert map["clientName"].baseAccessor.id().configuration.getClientId() == "clientName"

    assert map["clientName"].id().class == IdGateway.class
    assert map["clientName"].id().behaviors.size() == 1 // should get the root behaviors
    assert map["clientName"].accounts().class == AccountGateway.class
    assert map["clientName"].accounts().behaviors.size() == 2 // should get root behaviors plus the configured behaviors
    assert map["clientName"].accounts().services.size() == 1
    assert map["clientName"].accounts().transactions().class == TransactionGateway.class

    assert map["clientName"].accounts().getRemote().class == RemoteAccountGateway
    assert map["clientName"].accounts().transactions().getRemote().class == RemoteTransactionGateway
    assert ((RemoteAccountGateway) map["clientName"].accounts().getRemote()).getConfigurations() == new ObjectMap().tap {
      put("list", new ObjectMap())
    }
    assert ((RemoteTransactionGateway) map["clientName"].accounts().transactions().getRemote()).getConfigurations() == new ObjectMap().tap {
      put("search", new ObjectMap())
      put("recent", new ObjectMap())
    }

    assert Facilities.getCacheStore("clientName").class == StoreImpl.class
    assert Facilities.getSessionStore("clientName").class == StoreImpl.class
    assert Facilities.getEncryptionService("clientName").class == EncryptionServiceImpl.class
    assert Facilities.getMessageBroker("clientName").class == MessageBrokerImpl.class
    assert Facilities.getFaultTolerantExecutor("clientName").class == FaultTolerantExecutorImpl.class
    assert ((StoreImpl) Facilities.getCacheStore("clientName")).getConfigurations().get("key1") == "value1"
    assert ((StoreImpl) Facilities.getSessionStore("clientName")).getConfigurations().get("key1") == "value2"
    assert ((EncryptionServiceImpl) Facilities.getEncryptionService("clientName")).getConfigurations().get("key1") == "value3"
    assert ((MessageBrokerImpl) Facilities.getMessageBroker("clientName")).getConfigurations().getTestField() == "value4"
    assert ((FaultTolerantExecutorImpl) Facilities.getFaultTolerantExecutor("clientName")).getConfig().getClientId() == "clientName"
    assert ((FaultTolerantExecutorImpl) Facilities.getFaultTolerantExecutor("clientName")).getConfig().getTimeoutScope() == "http"

    // ensure behaviors are not shared
    Set<GatewayBehavior> behaviors = new HashSet<>()
    (map["clientName"].id().behaviors + map["clientName"].accounts().behaviors).each {behavior ->
      assert !behaviors.contains(behavior)
      behaviors.add(behavior)
    }

    return true
  }
}
