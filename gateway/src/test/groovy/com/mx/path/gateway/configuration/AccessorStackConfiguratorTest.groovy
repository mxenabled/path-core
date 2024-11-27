package com.mx.path.gateway.configuration

import static org.mockito.Mockito.mockStatic
import static org.mockito.Mockito.times

import java.lang.reflect.Method

import com.mx.path.core.common.accessor.AfterAccessorInitialize
import com.mx.path.gateway.accessor.Accessor

import org.mockito.MockedStatic

import spock.lang.Specification

class AccessorStackConfiguratorTest extends Specification {

  class MockAccessor extends Accessor {
    @AfterAccessorInitialize
    static void afterInitialize() {
    }

    @AfterAccessorInitialize
    static void afterInitializeWithClientId(String clientId) {
    }
  }

  class ChildMockAccessor extends MockAccessor {
    @AfterAccessorInitialize
    static void afterInitializeChild() {
    }

    @AfterAccessorInitialize
    static void afterInitializeChildWithClientId(String clientId) {
    }
  }

  AccessorStackConfigurator subject
  static MockedStatic<MockAccessor> mockAccessor
  static MockedStatic<ChildMockAccessor> childMockAccessor

  def setup() {
    subject = new AccessorStackConfigurator(ConfigurationState.getCurrent())
    mockAccessor.reset()
    childMockAccessor.reset()
  }

  def setupSpec() {
    mockAccessor = mockStatic(MockAccessor.class)
    childMockAccessor = mockStatic(ChildMockAccessor.class)
  }

  def cleanupSpec() {
    mockAccessor.close()
    childMockAccessor.close()
  }

  def "invoke afterInitializeMethods"() {
    when:
    Method method = subject.class.getDeclaredMethod("invokeAfterInitializeMethods", Class, String)
    method.setAccessible(true)
    method.invoke(subject, MockAccessor.class, "clientId")

    then:
    mockAccessor.verify({ MockAccessor.afterInitialize() }, times(1))
    mockAccessor.verify({ MockAccessor.afterInitializeWithClientId("clientId") }, times(1))
  }

  def "invoke both afterInitializeMethods"() {
    when:
    Method method = subject.class.getDeclaredMethod("invokeAfterInitializeMethods", Class, String)
    method.setAccessible(true)
    method.invoke(subject, ChildMockAccessor.class, "clientId")

    then:
    mockAccessor.verify({ MockAccessor.afterInitialize() }, times(1))
    childMockAccessor.verify({ ChildMockAccessor.afterInitializeChild() }, times(1))

    mockAccessor.verify({ MockAccessor.afterInitializeWithClientId("clientId") }, times(1))
    childMockAccessor.verify({ ChildMockAccessor.afterInitializeChildWithClientId("clientId") }, times(1))
  }
}
