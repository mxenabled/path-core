package com.mx.path.gateway.configuration

import com.mx.common.collections.ObjectMap
import com.mx.testing.binding.BasicConfigurationObj
import com.mx.testing.binding.RequireArrayConfiguration
import com.mx.testing.binding.RequireObjConfiguration
import com.mx.testing.binding.RequireStringFieldConfiguration
import com.mx.testing.binding.WithGenericList

import spock.lang.Specification

class ConfigurationBinderTest extends Specification {

  ObjectMap configuration
  ConfigurationState state
  ConfigurationBinder subject

  def setup() {
    state = new ConfigurationState()
    subject = new ConfigurationBinder("client1", state)
    configuration = new ObjectMap()
  }

  def "configure binds attributes to existing object"() {
    given:
    def configurationObj = new BasicConfigurationObj()

    configuration.put("key1", "value1")
    configuration.put("key2", 12)
    configuration.createArray("array1").tap {
      add("item1")
      add("item2")
    }

    when:
    subject.configure(configurationObj, configuration)

    then:
    configurationObj.getKey1() == "value1"
    configurationObj.getKey2() == 12
  }

  def "build creates and binds attributes to existing object"() {
    given:
    configuration.put("key1", "value1")
    configuration.put("key2", 12)

    when:
    BasicConfigurationObj configurationObj = subject.build(BasicConfigurationObj.class, configuration)

    then:
    configurationObj.getClientId() == "client1"
    configurationObj.getKey1() == "value1"
    configurationObj.getKey2() == 12
  }

  def "configure fails on keys that have no field to bind"() {
    given:
    def configurationObj = new BasicConfigurationObj()
    state.pushLevel("accessor")

    configuration.put("junk1", "value1")

    when:
    subject.configure(configurationObj, configuration)

    then:
    def error = thrown(ConfigurationError)
    error.getMessage() == "Unknown field on junk1 at accessor"
  }

  def "binds arrays"() {
    given:
    configuration.createArray("array1").tap {
      add("item1")
      add("item2")
    }

    when:
    BasicConfigurationObj configurationObj = subject.build(BasicConfigurationObj.class, configuration)

    then:
    configurationObj.getArray1().size() == 2
    configurationObj.getArray1().get(0) == "item1"
    configurationObj.getArray1().get(1) == "item2"
  }

  def "binds complex object"() {
    given:
    configuration.createMap("subConfig").tap{ put("subkey1", "subkeyValue1") }

    when:
    def configurationObj = subject.build(BasicConfigurationObj.class, configuration)

    then:
    configurationObj.getSubConfig().getSubkey1() == "subkeyValue1"
  }

  def "binds complex arrays"() {
    given:
    configuration.createArray("complexArray1").tap {
      createMap().tap{ put("subkey1", "subkeyValue1") }
      createMap().tap{ put("subkey1", "subkeyValue2") }
    }

    when:
    BasicConfigurationObj configurationObj = subject.build(BasicConfigurationObj.class, configuration)

    then:
    configurationObj.getComplexArray1().size() == 2
    configurationObj.getComplexArray1().get(0).getSubkey1() == "subkeyValue1"
    configurationObj.getComplexArray1().get(1).getSubkey1() == "subkeyValue2"
  }

  def "binds complex maps"() {
    given:
    configuration.put("deposit", new ObjectMap().tap{
      put("C", "checking")
      put("S", "savings")
    })

    when:
    BasicConfigurationObj configurationObj = subject.build(BasicConfigurationObj.class, configuration)

    then:
    configurationObj.getDeposit().size() == 2
    configurationObj.getDeposit().get("C") == "checking"
    configurationObj.getDeposit().get("S") == "savings"
  }

  def "validates required string field"() {
    given:
    state.pushLevel("test.configuration")

    when: "value is null"
    subject.build(RequireStringFieldConfiguration .class, configuration)

    then:
    def error = thrown(ConfigurationError)
    error.message == "Value required on key1 at test.configuration"

    when: "value is blank"
    configuration.put("key1", "")
    subject.build(RequireStringFieldConfiguration .class, configuration)

    then:
    error = thrown(ConfigurationError)
    error.message == "Value required on key1 at test.configuration"

    when: "value is provided"
    configuration.put("key1", "value1")
    def result = subject.build(RequireStringFieldConfiguration .class, configuration)

    then: "it passes"
    result.key1 == "value1"
  }

  def "validates required object"() {
    given:
    state.pushLevel("test.configuration")

    when: "Object missing"
    subject.build(RequireObjConfiguration.class, configuration)

    then: "validation fails"
    def error = thrown(ConfigurationError)
    error.message == "Value required on subConfig at test.configuration"

    when: "Object is fails validation"
    configuration.createMap("subConfig")
    subject.build(RequireObjConfiguration.class, configuration)

    then: "validation fails"
    error = thrown(ConfigurationError)
    error.message == "Value required on subkey1 at test.configuration.subConfig"

    when: "object provided"
    configuration.createMap("subConfig").tap { put("subkey1", "value1") }
    def result = subject.build(RequireObjConfiguration.class, configuration)

    then: "validation passes"
    result.subConfig != null
    result.subConfig.subkey1 == "value1"
  }

  def "validates required array"() {
    given:
    state.pushLevel("test.configuration")

    when:
    configuration = new ObjectMap()
    configuration.createArray("objArray").tap {
      add(new ObjectMap().tap { put("subkey1", "value1") })
    }

    subject.build(RequireArrayConfiguration.class, configuration)

    then:
    def error = thrown(ConfigurationError)
    error.message == "Value required on stringArray at test.configuration"

    when: "complex object inside array fails validation"
    configuration = new ObjectMap()
    configuration.createArray("stringArray").tap { add("value1") }
    configuration.createArray("objArray").tap {
      add(new ObjectMap())
    }

    subject.build(RequireArrayConfiguration.class, configuration)

    then:
    error = thrown(ConfigurationError)
    error.message == "Value required on subkey1 at test.configuration.objArray.0"

    when:
    configuration = new ObjectMap()
    configuration.createArray("stringArray").tap { add("value1") }

    subject.build(RequireArrayConfiguration.class, configuration)

    then:
    error = thrown(ConfigurationError)
    error.message == "Value required on objArray at test.configuration"

    when:
    configuration.createArray("stringArray").tap { add("value1") }
    configuration.createArray("objArray").tap {
      add(new ObjectMap().tap { put("subkey1", "value1") })
    }

    def result = subject.build(RequireArrayConfiguration.class, configuration)

    then:
    result.objArray.size() == 1
    result.stringArray.size() == 1
  }

  def "bind unannotated object"() {
    given:
    def configuration = new ObjectMap()
    configuration.createArray("list").tap {
      add(new ObjectMap().tap {
        put("id", 2)
        put("name", "Number 2")
      })
    }

    when:
    def result = subject.build(WithGenericList.class, configuration)

    then:
    result.list.size() == 1
  }
}
