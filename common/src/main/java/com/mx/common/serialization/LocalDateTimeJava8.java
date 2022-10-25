package com.mx.common.serialization;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Java 8 compatible LocalDateTime (de)serialization class.
 *
 * <p>Java 8 (Gson) serializes LocalDateTime as an object like this:
 *
 * <pre>{@code
 *   {
 *     "expiresAt": 1659984648,
 *     "id": "2d2c6f7b-f0b2-41fb-bfa5-e043d08fa8d7",
 *     "jointOwners": [],
 *     "sessionState": "UNAUTHENTICATED",
 *     "startedAt": {
 *       "date": {
 *         "year": 2022,
 *         "month": 8,
 *         "day": 8
 *       },
 *       "time": {
 *         "hour": 18,
 *         "minute": 20,
 *         "second": 48,
 *         "nano": 878000000
 *       }
 *     }
 *   }
 * }</pre>
 *
 * <p>This class is used to (de)serialize LocalDateTime to/from the Java 8 object format.
 *
 * <p>It is used by {@link LocalDateTimeDeserializer}
 */
@Data
class LocalDateTimeJava8 {
  @Data
  public static class Time {
    private int hour;
    private int minute;
    private int second;
    private int nano;
  }

  @Data
  public static class Date {
    private int year;
    private int month;
    private int day;
  }

  private Time time;
  private Date date;

  LocalDateTimeJava8() {
  }

  LocalDateTimeJava8(LocalDateTime localDateTime) {
    Date d = new Date();
    Time t = new Time();

    this.setDate(d);
    this.setTime(t);

    d.setYear(localDateTime.getYear());
    d.setMonth(localDateTime.getMonthValue());
    d.setDay(localDateTime.getDayOfMonth());
    t.setHour(localDateTime.getHour());
    t.setMinute(localDateTime.getMinute());
    t.setSecond(localDateTime.getSecond());
    t.setNano(localDateTime.getNano());
  }

  public final LocalDateTime toLocalDateTime() {
    return LocalDateTime.of(
        date.getYear(),
        date.getMonth(),
        date.getDay(),
        time.getHour(),
        time.getMinute(),
        time.getSecond(),
        time.getNano());
  }
}
