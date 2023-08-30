package com.mx.path.core.common.serialization;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import lombok.Data;

/**
 * Java 8 compatible OffsetDateTime (de)serialization class.
 *
 * <p>Java 8 (Gson) serializes OffsetDateTime as an object like this:
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
 *         "offset": "-06:00"
 *       }
 *     }
 *   }
 * }</pre>
 *
 * <p>This class is used to (de)serialize OffsetDateTime to/from the Java 8 object format.
 *
 * <p>It is used by {@link OffsetDateTimeDeserializer}
 */
@Data
class OffsetDateTimeJava8 {
  @Data
  public static class Time {
    private int hour;
    private int minute;
    private int second;
    private int nano;
    private ZoneOffset offset;
  }

  @Data
  public static class Date {
    private int year;
    private int month;
    private int day;
  }

  private Time time;
  private Date date;

  OffsetDateTimeJava8() {
  }

  OffsetDateTimeJava8(OffsetDateTime offsetDateTime) {
    Date d = new Date();
    Time t = new Time();

    this.setDate(d);
    this.setTime(t);

    d.setYear(offsetDateTime.getYear());
    d.setMonth(offsetDateTime.getMonthValue());
    d.setDay(offsetDateTime.getDayOfMonth());
    t.setHour(offsetDateTime.getHour());
    t.setMinute(offsetDateTime.getMinute());
    t.setSecond(offsetDateTime.getSecond());
    t.setNano(offsetDateTime.getNano());
    t.setOffset(offsetDateTime.getOffset());
  }

  public final OffsetDateTime toOffsetDateTime() {
    return OffsetDateTime.of(
        date.getYear(),
        date.getMonth(),
        date.getDay(),
        time.getHour(),
        time.getMinute(),
        time.getSecond(),
        time.getNano(),
        time.getOffset());
  }
}
