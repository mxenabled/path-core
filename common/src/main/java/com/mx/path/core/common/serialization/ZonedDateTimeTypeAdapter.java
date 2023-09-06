package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * ZonedDateTime TypeAdapter for use with Gson
 *
 * <p>Default Behavior:
 *
 * <ul>
 *   <li>Serialize ZonedDateTime to Object</li>
 *   <li>Allow deserialization of ZonedDateTime from object Object</li>
 *   <li>Allows deserialization of ZonedDateTime from from string format "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"</li>
 * </ul>
 *
 * <p>Features:
 *
 * <ul>
 *   <li>Serialize to OBJECT or a format string by setting {@link #serializeFormat}</li>
 *   <li>Deserialize from OBJECT or multiple format string by setting {@link #formats}</li>
 *   <li>Set default ZoneId to be used if no ZoneId is present</li>
 * </ul>
 *
 * <p>Examples:
 *
 * <pre>{@code
 *   // Take default behavior
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     ZonedDateTime.class,
 *     ZonedDateTimeDeserializer.builder().build()
 *   ).create();
 *
 *   // Add acceptable deserialization formats (will still handle object)
 *   // Removes default format. Needs to be added using .format() if still needed.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     ZonedDateTime.class,
 *     ZonedDateTimeDeserializer.builder()
 *       .format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
 *       .build()
 *   ).create();
 *
 *   // Serialize as String
 *   // Must add it to accepted formats using .format in order to deserialize.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     ZonedDateTime.class,
 *     ZonedDateTimeDeserializer.builder()
 *       .serializeFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
 *       .format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
 *       .build()
 *   ).create();
 * }</pre>
 */
@Builder
public class ZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  private static final List<DateTimeFormatter> DEFAULT_FORMAT_STRINGS;
  static {
    List<DateTimeFormatter> dateTimeFormatters = new ArrayList<>();
    dateTimeFormatters.add(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));

    DEFAULT_FORMAT_STRINGS = Collections.unmodifiableList(dateTimeFormatters);
  }

  private static final ZoneId UTC = ZoneId.of("UTC");

  private ZoneId defaultZoneId;

  private List<DateTimeFormatter> formats;

  private String serializeFormat;

  public static class ZonedDateTimeTypeAdapterBuilder {

    private ZoneId defaultZoneId = UTC;
    private List<DateTimeFormatter> formats = new ArrayList<>();

    private String serializeFormat = "OBJECT";

    /**
     * Provide a ZoneId to be used if no ZoneId is present
     *
     * @param zoneId ZoneID
     * @return builder
     */
    public final ZonedDateTimeTypeAdapterBuilder defaultZoneId(ZoneId zoneId) {
      defaultZoneId = zoneId;
      return this;
    }

    /**
     * Provide a ZonedDateTime string format that is accepted
     *
     * <p>The formats need to be provided according to {@link DateTimeFormatter} specs.
     *
     * @param format Format string of acceptable DateTime format
     * @return builder
     */
    public final ZonedDateTimeTypeAdapterBuilder format(String format) {
      formats.add(DateTimeFormatter.ofPattern(format));
      return this;
    }

    /**
     * Provide format for serializing a ZonedDateTime object to JSON
     *
     * @param format A DateTimeFormatter format String or
     *               OBJECT to serialize as an object (default)
     * @return builder
     */
    public final ZonedDateTimeTypeAdapterBuilder serializeFormat(String format) {
      serializeFormat = format;
      return this;
    }
  }

  static class LocalTimeWithZone {
    @Getter
    private final LocalTime time;

    @Getter
    private final ZoneId zoneId;

    LocalTimeWithZone(LocalTime time, ZoneId zoneId) {
      this.time = time;
      this.zoneId = zoneId;
    }
  }

  @Override
  public final void write(JsonWriter out, ZonedDateTime value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    if ("OBJECT".equals(serializeFormat)) {
      writeDateTimeObject(out, value);
    } else {
      out.value(value.format(DateTimeFormatter.ofPattern(serializeFormat)));
    }
  }

  @Override
  public final ZonedDateTime read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.skipValue();
      return null;
    }

    ZonedDateTime zonedDateTime = readDateTimeObject(in);
    if (zonedDateTime != null) {
      return zonedDateTime;
    }

    String zonedDateTimeStr = in.nextString();
    for (DateTimeFormatter s : formats.isEmpty() ? DEFAULT_FORMAT_STRINGS : formats) {
      try {
        return ZonedDateTime.parse(zonedDateTimeStr, s);
      } catch (DateTimeParseException ignored) {
      }
    }

    throw new JsonParseException("Invalid date time: " + zonedDateTimeStr);
  }

  private ZonedDateTime readDateTimeObject(JsonReader in) throws IOException {
    try {
      in.beginObject();
      LocalDate date = null;
      LocalTimeWithZone time = null;

      while (in.hasNext()) {
        switch (in.nextName()) {
          case "date":
            date = readDateObject(in);
            break;
          case "time":
            time = readTimeObject(in);
            break;
          default:
            in.skipValue();
            break;
        }
      }
      in.endObject();

      if (date == null) {
        throw new SerializationException("Unable to deserialize date portion of ZonedDateTime");
      }

      if (time == null) {
        throw new SerializationException("Unable to deserialize time portion of ZonedDateTime");
      }

      return ZonedDateTime.of(date, time.time, time.zoneId);
    } catch (IllegalStateException ignored) {
    }

    return null;
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private LocalDate readDateObject(JsonReader in) throws IOException {
    try {
      Integer year = null;
      Integer month = null;
      Integer day = null;

      in.beginObject();
      while (in.hasNext()) {
        switch (in.nextName()) {
          case "year":
            year = in.nextInt();
            break;
          case "month":
            month = in.nextInt();
            break;
          case "day":
            day = in.nextInt();
            break;
          default:
            in.skipValue();
        }
      }
      in.endObject();

      if (year == null) {
        throw new SerializationException("Missing year from ZonedDateTime object");
      }
      if (month == null) {
        throw new SerializationException("Missing month from ZonedDateTime object");
      }
      if (day == null) {
        throw new SerializationException("Missing day from ZonedDateTime object");
      }

      return LocalDate.of(year, month, day);
    } catch (IllegalStateException ignored) {
    }

    return null;
  }

  private LocalTimeWithZone readTimeObject(JsonReader in) throws IOException {
    try {
      int hour = 0;
      int minute = 0;
      int second = 0;
      int nano = 0;
      ZoneId zoneId = defaultZoneId;

      in.beginObject();
      while (in.hasNext()) {
        switch (in.nextName()) {
          case "hour":
            hour = in.nextInt();
            break;
          case "minute":
            minute = in.nextInt();
            break;
          case "second":
            second = in.nextInt();
            break;
          case "nano":
            nano = in.nextInt();
            break;
          case "zone":
            zoneId = ZoneId.of(in.nextString());
            break;
          default:
            in.skipValue();
        }
      }
      in.endObject();

      return new LocalTimeWithZone(LocalTime.of(hour, minute, second, nano), zoneId);
    } catch (IllegalStateException ignored) {
    }

    return null;
  }

  private void writeDateTimeObject(JsonWriter out, ZonedDateTime value) throws IOException {
    out.beginObject();

    out.name("time").beginObject();
    out.name("hour").value(value.getHour());
    out.name("minute").value(value.getMinute());
    out.name("second").value(value.getSecond());
    out.name("nano").value(value.getNano());
    out.name("zone").value(value.getZone().getId());
    out.endObject();

    out.name("date").beginObject();
    out.name("year").value(value.getYear());
    out.name("month").value(value.getMonthValue());
    out.name("day").value(value.getDayOfMonth());
    out.endObject();

    out.endObject();
  }
}
