package com.mx.path.core.common.lang;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mx.path.core.common.configuration.ConfigurationException;

/**
 * Duration helpers
 *
 * <p>Provides helpers for dealing with {@link Duration}. Primarily provides support for compact duration representations used
 * in the Path configuration layer.
 *
 * <p><i>Supported duration string format examples</i>
 *  <ul>
 *    <li>{@code 30s} - 30 seconds
 *    <li>{@code 30m} - 30 milliseconds
 *    <li>{@code 5min} - 5 minutes
 *  </ul>
 *
 *  <p><i>Supported unit specifications</i>
 *  <ul>
 *    <li> {@code "s", "sec", "seconds"} - <i>seconds</i>
 *    <li> {@code "m", "ms", "millis", "milliseconds"} - <i>milliseconds</i>
 *    <li> {@code "n", "nanos", "nanoseconds"} - <i>nanoseconds</i>
 *    <li> {@code "min", "minutes"} - <i>minutes (not, this must be more fully specified to avoid conflict with milliseconds</i>
 *    <li> {@code "h", "hours"} - <i>hours</i>
 *    <li> {@code "d", "days"} - <i>days</i>
 *  </ul>
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public class Durations {

  /**
   * These suffixes are used to render compact duration strings but are also the minimum
   * suffix used to match when parsing a compact duration string
   *
   * <p>For example: "10m", "10millis", and "10milliseconds" all parse to a {@code Duration.ofMillis(10)}
   */
  private static final String DAYS_MINIMUM_SUFFIX = "d";
  private static final String HOURS_MINIMUM_SUFFIX = "h";
  private static final String MINUTES_MINIMUM_SUFFIX = "min";
  private static final String SECONDS_MINIMUM_SUFFIX = "s";
  private static final String MILLIS_MINIMUM_SUFFIX = "m";
  private static final String NANOS_MINIMUM_SUFFIX = "n";

  private static final Pattern DURATION_PATTERN = Pattern.compile("^(?<value>\\d+)\\s*(?<unit>[a-z]+)$");

  /**
   * Parse given compact duration string to {@link Duration}.
   *
   * @param durationStr compact duration string
   * @return duration representing compact string
   */
  public static Duration fromCompactString(String durationStr) {
    Matcher matcher = DURATION_PATTERN.matcher(durationStr.toString().trim());
    try {
      if (matcher.matches()) {
        long durationValue = Integer.parseInt(matcher.group("value"));
        String durationUnit = matcher.group("unit");

        if (durationUnit.startsWith(DAYS_MINIMUM_SUFFIX)) {
          return Duration.ofDays(durationValue);
        }

        if (durationUnit.startsWith(HOURS_MINIMUM_SUFFIX)) {
          return Duration.ofHours(durationValue);
        }

        if (durationUnit.startsWith(MINUTES_MINIMUM_SUFFIX)) {
          return Duration.ofMinutes(durationValue);
        }

        if (durationUnit.startsWith(MILLIS_MINIMUM_SUFFIX)) {
          return Duration.ofMillis(durationValue);
        }

        if (durationUnit.startsWith(SECONDS_MINIMUM_SUFFIX)) {
          return Duration.ofSeconds(durationValue);
        }

        if (durationUnit.startsWith(NANOS_MINIMUM_SUFFIX)) {
          return Duration.ofNanos(durationValue);
        }

        throw new ConfigurationException("Invalid duration unit: " + durationStr);
      } else {
        throw new ConfigurationException("Invalid duration string: " + durationStr);
      }
    } catch (NumberFormatException e) {
      throw new ConfigurationException("Invalid duration string: " + durationStr, e);
    }
  }

  /**
   * Calculate the largest unit that can accurately represent given {@link Duration}
   *
   * @param duration Duration
   * @return largest accurate unit as {@link ChronoUnit}
   */
  public static ChronoUnit maxAccurateUnit(Duration duration) {
    Duration reducingDuration = Duration.ofSeconds(duration.getSeconds(), duration.getNano());

    if (reducingDuration.toDays() > 0) {
      reducingDuration = reducingDuration.minusDays(duration.toDays());
      if (reducingDuration.toNanos() <= 0) {
        return ChronoUnit.DAYS;
      }
    }

    if (reducingDuration.toHours() > 0) {
      reducingDuration = reducingDuration.minusHours(duration.toHours());
      if (reducingDuration.toNanos() <= 0) {
        return ChronoUnit.HOURS;
      }
    }

    if (reducingDuration.toMinutes() > 0) {
      reducingDuration = reducingDuration.minusMinutes(duration.toMinutes());
      if (reducingDuration.toNanos() <= 0) {
        return ChronoUnit.MINUTES;
      }
    }

    if (reducingDuration.getSeconds() > 0) {
      reducingDuration = reducingDuration.minusSeconds(duration.getSeconds());
      if (reducingDuration.toNanos() <= 0) {
        return ChronoUnit.SECONDS;
      }
    }

    if (reducingDuration.toMillis() > 0) {
      reducingDuration = reducingDuration.minusMillis(duration.toMillis());
      if (reducingDuration.toNanos() <= 0) {
        return ChronoUnit.MILLIS;
      }
    }

    return ChronoUnit.NANOS;
  }

  /**
   * Convert given {@link Duration} to an accurate compact string representation. Renders using the largest accurate unit.
   * @param duration Duration
   * @return compact duration string
   */
  public static String toCompactString(Duration duration) {
    return toCompactString(duration, maxAccurateUnit(duration));
  }

  /**
   * Convert given {@link Duration} to a compact string using given {@link ChronoUnit}. May not be accurate representation.
   * @param duration Duration
   * @param unit unit to us in compact string
   * @return compact duration string
   */
  public static String toCompactString(Duration duration, ChronoUnit unit) {
    switch (unit) {
      case DAYS:
        return duration.toDays() + DAYS_MINIMUM_SUFFIX;
      case HOURS:
        return duration.toHours() + HOURS_MINIMUM_SUFFIX;
      case MINUTES:
        return duration.toMinutes() + MINUTES_MINIMUM_SUFFIX;
      case SECONDS:
        return duration.getSeconds() + SECONDS_MINIMUM_SUFFIX;
      case MILLIS:
        return duration.toMillis() + MILLIS_MINIMUM_SUFFIX;
      default:
        return duration.toNanos() + NANOS_MINIMUM_SUFFIX;
    }
  }
}
