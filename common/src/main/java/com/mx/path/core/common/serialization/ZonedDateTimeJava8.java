package com.mx.path.core.common.serialization;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.Data;

/**
 * Java 8 compatible ZonedDateTime (de)serialization class.
 *
 * <p>Java 8 (Gson) serializes ZonedDateTime as an object like this:
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
 *         "nano": 878000000,
 *         "zone":"-06:00"
 *       }
 *     }
 *   }
 * }</pre>
 *
 * <p>This class is used to (de)serialize ZonedDateTime to/from the Java 8 object format.
 *
 * <p>It is used by {@link ZonedDateTimeDeserializer}
 */
@Data
class ZonedDateTimeJava8 {
  @Data
  public static class Time {
    private int hour;
    private int minute;
    private int second;
    private int nano;
    private ZoneId zone;
  }

  @Data
  public static class Date {
    private int year;
    private int month;
    private int day;
  }

  private Time time;
  private Date date;

  ZonedDateTimeJava8() {
  }

  ZonedDateTimeJava8(ZonedDateTime zonedDateTime) {
    Date d = new Date();
    Time t = new Time();

    this.setDate(d);
    this.setTime(t);

    d.setYear(zonedDateTime.getYear());
    d.setMonth(zonedDateTime.getMonthValue());
    d.setDay(zonedDateTime.getDayOfMonth());
    t.setHour(zonedDateTime.getHour());
    t.setMinute(zonedDateTime.getMinute());
    t.setSecond(zonedDateTime.getSecond());
    t.setNano(zonedDateTime.getNano());
    t.setZone(zonedDateTime.getZone());
  }

  public final ZonedDateTime toZonedDateTime() {
    return ZonedDateTime.of(
        date.getYear(),
        date.getMonth(),
        date.getDay(),
        time.getHour(),
        time.getMinute(),
        time.getSecond(),
        time.getNano(),
        time.getZone());
  }
}
