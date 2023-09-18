package com.mx.path.core.common.process

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import java.time.Duration
import java.util.concurrent.Callable

import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.accessor.UpstreamSystemUnavailable
import com.mx.path.core.common.connect.UpstreamErrorException
import com.mx.path.core.common.http.HttpStatus
import com.mx.path.gateway.configuration.ConfigurationError
import com.mx.path.gateway.configuration.ConfigurationState
import com.mx.testing.WithMockery

import spock.lang.Specification
import spock.lang.Unroll

class BlockRetryConfigurationTest extends Specification implements WithMockery {

  def "fixed sleep retry"() {
    when:
    def subject = BlockRetryConfiguration.<Boolean>builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(2)
        .rejectOn({ t -> true })
        .build()

    def callCount = 0
    subject.call({ b ->
      callCount = callCount + 1
    })

    then:
    thrown(RetriesFailedException)
    callCount == 2
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
    configuration                                                                                                                                                                       | message
    BlockRetryConfiguration.builder().build()                                                                                                                                           | "Missing required field at configuration."
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).build()                                                                               | "Missing required fields for stopStrategy COUNT: count at configuration."
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.DURATION).build()                                                                            | "Missing required fields for stopStrategy DURATION: duration at configuration."
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIXED).build()        | "Missing required fields for pauseStrategy FIXED: pause at configuration."
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.INCREMENTING).build() | "Missing required fields for pauseStrategy INCREMENTING: initialPause, increment at configuration."
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIBONACCI).build()    | "Missing required fields for pauseStrategy FIBONACCI: multiplier, maxPause at configuration."
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
    configuration                                                                                                                                                                                                                                            | _
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).build()                                                                                                                                           | null
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.DURATION).duration(Duration.ofSeconds(1)).build()                                                                                                                 | null
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIXED).pause(Duration.ofMillis(10)).build()                                                | null
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.INCREMENTING).initialPause(Duration.ofMillis(10)).increment(Duration.ofMillis(10)).build() | null
    BlockRetryConfiguration.builder().stopStrategy(RetryConfiguration.StopStrategy.COUNT).count(1).pauseStrategy(RetryConfiguration.PauseStrategy.FIBONACCI).maxPause(Duration.ofMillis(100)).multiplier(Duration.ofSeconds(1)).build()      | null
  }

  def "call when all attempts fail"() {
    given:
    def subject = BlockRetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(3)
        .rejectOn({ t -> true })
        .build()

    when:
    subject.call({
      ->
      null
    } as Callable)

    then:
    def error = thrown(RetriesFailedException)
    error.numberOfFailedAttempts == 3
  }

  def "when call throws exception"() {
    given:
    def subject = BlockRetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(1)
        .rejectOn({ t -> false })
        .build()

    when: "Throws a RuntimeException"
    subject.call({ t ->
      throw new UpstreamErrorException("", HttpStatus.BAD_GATEWAY, PathResponseStatus.OK)
    })

    then: "Rethrows actual"
    thrown(UpstreamErrorException)

    when: "Throws Exception"
    subject.call({ t ->
      throw new InvalidClassException("", "")
    })

    then: "Wraps in RuntimeException"
    def err = thrown(RuntimeException)
    err.cause.class == InvalidClassException
  }

  def "call when attempt succeeds"() {
    given:
    def subject = BlockRetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(3)
        .rejectOn({ t -> false })
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

  def "builder"() {
    when:
    BlockRetryConfiguration<Boolean> result = BlockRetryConfiguration.<Boolean>builder()
        .count(1)
        .duration(Duration.ofSeconds(1))
        .exceptionSupplier({ e -> new UpstreamSystemUnavailable("") })
        .increment(Duration.ofSeconds(1))
        .initialPause(Duration.ofSeconds(1))
        .maxPause(Duration.ofSeconds(1))
        .multiplier(Duration.ofSeconds(1))
        .pause(Duration.ofSeconds(1))
        .pauseStrategy(RetryConfiguration.PauseStrategy.FIXED)
        .rejectOn({ t -> true })
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .build()

    then:
    result.instance() != null
    verifyAll(result) {
      count == 1
      duration == Duration.ofSeconds(1)
      exceptionSupplier.apply(null) instanceof UpstreamSystemUnavailable
      increment == Duration.ofSeconds(1)
      initialPause == Duration.ofSeconds(1)
      maxPause == Duration.ofSeconds(1)
      multiplier == Duration.ofSeconds(1)
      pause == Duration.ofSeconds(1)
      pauseStrategy == RetryConfiguration.PauseStrategy.FIXED
      rejectOn.test(null)
      stopStrategy == RetryConfiguration.StopStrategy.COUNT
    }
  }
}
