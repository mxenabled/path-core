package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * LocalDateTime deserializer for use with Gson
 *
 * <p>Default Behavior:
 *
 * <ul>
 *   <li>Serialize LocalDateTime to Object</li>
 *   <li>Allow deserialization of LocalDateTime from object Object</li>
 *   <li>Allows deserialization of LocalDateTime from from string format "yyyy-MM-dd'T'HH:mm:ss.SSS"</li>
 * </ul>
 *
 * <p>Features:
 *
 * <ul>
 *   <li>Serialize to OBJECT or a format string by setting {@link #serializeFormat}</li>
 *   <li>Deserialize from OBJECT or multiple format string by setting {@link #formats}</li>
 * </ul>
 *
 * <p>Examples:
 *
 * <pre>{@code
 *   // Take default behavior
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDateTime.class,
 *     LocalDateTimeTypeAdapter.builder().build()
 *   ).create();
 *
 *   // Add acceptable deserialization formats (will still handle object)
 *   // Removes default format. Needs to be added using .format() if still needed.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDateTime.class,
 *     LocalDateTimeTypeAdapter.builder()
 *       .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
 *       .build()
 *   ).create();
 *
 *   // Serialize as String
 *   // Must add it to accepted formats using .format in order to deserialize.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDateTime.class,
 *     LocalDateTimeTypeAdapter.builder()
 *       .serializeFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
 *       .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
 *       .build()
 *   ).create();
 * }</pre>
 */
@Builder
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  private static final List<DateTimeFormatter> DEFAULT_FORMAT_STRINGS;

  static {
    List<DateTimeFormatter> dateTimeFormatters = new ArrayList<>();
    dateTimeFormatters.add(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));

    DEFAULT_FORMAT_STRINGS = Collections.unmodifiableList(dateTimeFormatters);
  }

  private static final Gson GSON = new Gson();

  private List<DateTimeFormatter> formats;

  private String serializeFormat;

  public static class LocalDateTimeTypeAdapterBuilder {

    private List<DateTimeFormatter> formats = new ArrayList<>();

    private String serializeFormat = "OBJECT";

    /**
     * Provide a LocalDate string format that is accepted. (default: "yyyy-MM-dd")
     *
     * <p>The formats need to be provided according to {@link DateTimeFormatter} specs.
     *
     * <p>Providing any formats will remove the default format.
     *
     * @param format Format string of acceptable LocalDate format
     * @return
     */
    public final LocalDateTimeTypeAdapterBuilder format(String format) {
      formats.add(DateTimeFormatter.ofPattern(format));
      return this;
    }

    /**
     * Provide format for serializing a LocalDate object to JSON
     *
     * @param format A DateTimeFormatter format String or
     *               OBJECT to serialize as an object (default)
     * @return builder
     */
    public final LocalDateTimeTypeAdapterBuilder serializeFormat(String format) {
      serializeFormat = format;
      return this;
    }
  }

  @Override
  public final void write(JsonWriter out, LocalDateTime value) throws IOException {
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
  public final LocalDateTime read(JsonReader in) throws IOException {
    if (in.peek() == null) {
      in.skipValue();
      return null;
    }

    LocalDateTime localDateTime = readDateTimeObject(in);
    if (localDateTime != null) {
      return localDateTime;
    }

    String localDateTimeStr = in.nextString();
    for (DateTimeFormatter s : formats.isEmpty() ? DEFAULT_FORMAT_STRINGS : formats) {
      try {
        return LocalDateTime.parse(localDateTimeStr, s);
      } catch (DateTimeParseException ignored) {
      }
    }

    throw new JsonParseException("Invalid date time: " + localDateTimeStr);
  }

  private LocalDateTime readDateTimeObject(JsonReader in) throws IOException {
    try {
      in.beginObject();
      LocalDate date = null;
      LocalTime time = null;

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
        throw new SerializationException("Unable to deserialize date portion of LocalDateTime");
      }

      if (time == null) {
        throw new SerializationException("Unable to deserialize time portion of LocalDateTime");
      }

      return LocalDateTime.of(date, time);
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
        throw new SerializationException("Missing year from LocalDateTime object");
      }
      if (month == null) {
        throw new SerializationException("Missing month from LocalDateTime object");
      }
      if (day == null) {
        throw new SerializationException("Missing day from LocalDateTime object");
      }

      return LocalDate.of(year, month, day);
    } catch (IllegalStateException ignored) {
    }

    return null;
  }

  private LocalTime readTimeObject(JsonReader in) throws IOException {
    try {
      int hour = 0;
      int minute = 0;
      int second = 0;
      int nano = 0;

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
          default:
            in.skipValue();
        }
      }
      in.endObject();

      return LocalTime.of(hour, minute, second, nano);
    } catch (IllegalStateException ignored) {
    }

    return null;
  }

  private void writeDateTimeObject(JsonWriter out, LocalDateTime value) throws IOException {
    out.beginObject();

    out.name("time").beginObject();
    out.name("hour").value(value.getHour());
    out.name("minute").value(value.getMinute());
    out.name("second").value(value.getSecond());
    out.name("nano").value(value.getNano());
    out.endObject();

    out.name("date").beginObject();
    out.name("year").value(value.getYear());
    out.name("month").value(value.getMonthValue());
    out.name("day").value(value.getDayOfMonth());
    out.endObject();

    out.endObject();
  }
}
