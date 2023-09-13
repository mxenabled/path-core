package com.mx.path.core.common.connect

import static org.mockito.Mockito.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import java.time.Duration
import java.util.concurrent.Callable

import com.github.rholder.retry.RetryException
import com.mx.path.gateway.configuration.ConfigurationError
import com.mx.path.gateway.configuration.ConfigurationState
import com.mx.testing.WithMockery
import com.mx.testing.connect.TestRequest

import spock.lang.Specification
import spock.lang.Unroll

class RetryConfigurationTest extends Specification implements WithMockery {

  def subject
  Request request
  RequestFilter requestFilter

  def setup() {
    requestFilter = mock(RequestFilter)
    request = new TestRequest(requestFilter)
  }

  def "fixed sleep retry"() {
    given:
    subject = RetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(2)
        .retryOn(Collections.singletonList(ResponseMatcher.builder().predicate({ t -> true }).build()))
        .build()

    request.withRetryer(subject.instance())

    when:
    request.execute()

    then: "executes 2 times"
    verify(requestFilter, times(2)).execute(any(Request), any(Response))
  }

  def "defaults"() {
    given:
    def configurationState = mock(ConfigurationState)
    subject = RetryConfiguration.defaults()
    request.withRetryer(subject.instance())

    when: "validate default configuration"
    subject.validate(configurationState)

    then: "is valid"
    noExceptionThrown()

    when: "execute request with default configuration"
    request.execute()

    then: "executes one time"
    verify(requestFilter, times(1)).execute(any(Request), any(Response))
  }

  def "verify"() {
    given:
    def configurationState = mock(ConfigurationState)

    when: "default configuration"
    RetryConfiguration.defaults().validate(configurationState)

    then: "is valid"
    noExceptionThrown()
  }

  @Unroll
  def "validation errors"() {
    given:
    def configurationState = mock(ConfigurationState)
    when(configurationState.currentState()).thenReturn("configuration.")

    when:
    configuration.validate(configurationState)

    then:
    def error = thrown(ConfigurationError)
    error.message == message

    where:
    configuration                                                                                                                                                | message
    RetryConfiguration.builder().build()                                                                                                                         | "Missing required field at configuration."
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).build()                                                                      | "Missing required fields for stopStrategy COUNT: count at configuration."
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.DURATION).build()                                                                   | "Missing required fields for stopStrategy DURATION: duration at configuration."
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIXED).build()        | "Missing required fields for pauseStrategy FIXED: pause at configuration."
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.INCREMENTING).build() | "Missing required fields for pauseStrategy INCREMENTING: initialPause, increment at configuration."
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIBONACCI).build()    | "Missing required fields for pauseStrategy FIBONACCI: multiplier, maxPause at configuration."
  }

  @Unroll
  def "valid configurations"() {
    given:
    def configurationState = mock(ConfigurationState)
    when(configurationState.currentState()).thenReturn("configuration.")

    when:
    configuration.validate(configurationState)

    then:
    noExceptionThrown()

    where:
    configuration                                                                                                                                                                                                                     | _
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).build()                                                                                                                                  | null
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.DURATION).duration(Duration.ofSeconds(1)).build()                                                                                                        | null
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIXED).pause(Duration.ofMillis(10)).build()                                                | null
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.INCREMENTING).initialPause(Duration.ofMillis(10)).increment(Duration.ofMillis(10)).build() | null
    RetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIBONACCI).maxPause(Duration.ofMillis(100)).multiplier(Duration.ofSeconds(1)).build()      | null
  }

  def "call when all attempts fail"() {
    given:
    def subject = RetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(3)
        .retryOn(Collections.singletonList(ResponseMatcher.builder().predicate({ t -> true }).build()))
        .build()

    when:
    subject.call({
      ->
      null
    } as Callable)

    then:
    def error = thrown(RetryException)
    error.numberOfFailedAttempts == 3
  }

  def "call when attempt succeeds"() {
    given:
    def subject = RetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(3)
        .retryOn(Collections.singletonList(ResponseMatcher.builder().predicate({ t -> false }).build()))
        .build()

    def callCount = 0

    when:
    subject.call({
      ->
      callCount = callCount + 1
      null
    } as Callable)

    then:
    noExceptionThrown()
    callCount == 1
  }
}
