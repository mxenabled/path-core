package com.mx.common.lang

import java.time.Duration
import java.time.temporal.ChronoUnit

import com.mx.common.reflection.Fields

import spock.lang.Specification

class DurationsTest extends Specification {
  def ".maxAccurateUnit"() {
    when:
    def result = Durations.maxAccurateUnit(duration)

    then:
    result == unit

    where:
    duration                              | unit
    Duration.ofDays(2)                    | ChronoUnit.DAYS
    Duration.ofDays(1).plusHours(1)       | ChronoUnit.HOURS
    Duration.ofDays(2).plusMillis(500)    | ChronoUnit.MILLIS
    Duration.ofSeconds(10)                | ChronoUnit.SECONDS
    Duration.ofSeconds(62)                | ChronoUnit.SECONDS
    Duration.ofMillis(10)                 | ChronoUnit.MILLIS
    Duration.ofNanos(100000000)           | ChronoUnit.MILLIS
    Duration.ofNanos(100000001)           | ChronoUnit.NANOS
  }

  def ".toCompactString"() {
    when:
    def result = Durations.toCompactString(duration)

    then:
    result == compactString

    where:
    duration                              | compactString
    Duration.ofDays(2)                    | "2d"
    Duration.ofDays(1).plusHours(1)       | "25h"
    Duration.ofDays(2).plusMillis(500)    | "172800500m"
    Duration.ofMinutes(10)                | "10min"
    Duration.ofMinutes(1).plusSeconds(2)  | "62s"
    Duration.ofSeconds(10)                | "10s"
    Duration.ofMillis(10)                 | "10m"
    Duration.ofNanos(100000000)           | "100m"
    Duration.ofNanos(100000001)           | "100000001n"
  }

  def ".fromCompactString"() {
    when:
    def result = Durations.fromCompactString(val)

    then:
    result == expected

    where:
    val                | expected
    " 10 s "           | Duration.ofSeconds(10)
    " 10sec "          | Duration.ofSeconds(10)
    " 10 m "           | Duration.ofMillis(10)
    " 10ms "           | Duration.ofMillis(10)
    " 10milliseconds " | Duration.ofMillis(10)
    " 10min "          | Duration.ofMinutes(10)
    " 10nanos "        | Duration.ofNanos(10)
    " 10h "            | Duration.ofHours(10)
  }
}
