package com.mx.path.gateway.configuration

import com.mx.path.core.common.collection.ObjectArray
import com.mx.path.core.common.collection.ObjectMap
import com.mx.testing.binding.BehaviorWithConfiguration
import com.mx.testing.binding.BehaviorWithObjectMap

import spock.lang.Specification

class BehaviorStackConfiguratorTest extends Specification {
  BehaviorStackConfigurator subject
  ConfigurationState state

  def setup() {
    state = ConfigurationState.current
    state.pushLevel("client1")
    subject = new BehaviorStackConfigurator(state)
  }

  def cleanup() {
    ConfigurationState.resetCurrent()
  }

  def "builds With no args"() {
    given:
    def configuration = new ObjectMap()
    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", com.mx.testing.binding.BehaviorWithNoArgs.getCanonicalName())
        put("configurations", configuration)
      })
    }

    when:
    def result = subject.buildFromNode(node, "client1")

    then:
    result != null
    result.get(0).getConfigurations() == configuration
  }

  def "builds With ObjectMap"() {
    given:
    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", BehaviorWithObjectMap.getCanonicalName())
        put("configurations", new ObjectMap())
      })
    }

    when:
    def result = subject.buildFromNode(node, "client1")

    then:
    result != null
  }

  def "builds With ClientID and Configuration"() {
    given:
    def configuration = new ObjectMap().tap {
      put("active", true)
      put("actionFilter", "put")
    }
    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", BehaviorWithConfiguration.getCanonicalName())
        put("configurations", configuration)
      })
    }

    when:
    def result = subject.buildFromNode(node, "client1")

    then:
    result.get(0).getClass() == BehaviorWithConfiguration
    result.get(0).class == BehaviorWithConfiguration
    result.get(0).getBehaviorConfiguration().getActionFilter() == "put"
    result.get(0).getBehaviorConfiguration().isActive()
    result.get(0).getConfigurations() == configuration
  }

  def "builds With Root Behaviors"() {
    given:
    def rootBehaviorsNode = new ObjectArray().tap {
      add(new ObjectMap().tap {
        put("class", BehaviorWithObjectMap.getCanonicalName())
        put("configurations", new ObjectMap())
      })
    }
    subject.setRootBehaviors(rootBehaviorsNode)

    def configuration = new ObjectMap().tap {
      put("active", true)
      put("actionFilter", "put")
    }

    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", BehaviorWithConfiguration.getCanonicalName())
        put("configurations", configuration)
      })
    }

    when:
    def result = subject.buildFromNode(node, "client1")

    then:
    result.size() == 2
    result.get(0).class == BehaviorWithObjectMap
    result.get(1).class == BehaviorWithConfiguration
  }

  def "no behaviors"() {
    given:
    def node = new ObjectMap()

    when:
    def result = subject.buildFromNode(node, "client1")

    then:
    result.size() == 0
  }

  def "error on not a GatewayBehavior"() {
    given:
    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", com.mx.testing.binding.BehaviorInvalidNotBehavior.getCanonicalName())
        put("configurations", new ObjectMap())
      })
    }

    when:
    subject.buildFromNode(node, "client1")

    then:
    def error = thrown(ConfigurationError)
    error.message == "com.mx.path.gateway.behavior.GatewayBehavior is not assignable from com.mx.testing.binding.BehaviorInvalidNotBehavior at client1.behaviors"
  }

  def "error on too many valid constructors"() {
    given:
    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", com.mx.testing.binding.BehaviorInvalidTooManyConstructors.getCanonicalName())
        put("configurations", new ObjectMap())
      })
    }

    when:
    subject.buildFromNode(node, "client1")

    then:
    def error = thrown(ConfigurationError)
    error.message == "Too many valid constructors for com.mx.testing.binding.BehaviorInvalidTooManyConstructors at client1.behaviors.BehaviorInvalidTooManyConstructors"
  }

  def "error on no valid constructors"() {
    given:
    def node = new ObjectMap()
    node.createArray("behaviors").tap {
      add(new ObjectMap().tap {
        put("class", com.mx.testing.binding.BehaviorInvalidNoValidConstructor.getCanonicalName())
        put("configurations", new ObjectMap())
      })
    }

    when:
    subject.buildFromNode(node, "client1")

    then:
    def error = thrown(ConfigurationError)
    error.message == "No valid constructors for com.mx.testing.binding.BehaviorInvalidNoValidConstructor at client1.behaviors.BehaviorInvalidNoValidConstructor"
  }
}
