package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.LocalDate;
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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * LocalDate TypeAdapter for use with Gson
 *
 * <p>Default Behavior:
 *
 * <ul>
 *   <li>Serialize LocalDate to Object</li>
 *   <li>Allow deserialization of LocalDate from object Object</li>
 *   <li>Allow deserialization of LocalDate from from string format "yyyy-MM-dd"</li>
 * </ul>
 *
 * <p>Examples:
 *
 * <pre>{@code
 *   // Take default behavior
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDate.class,
 *     LocalDateTypeAdapter.builder().build()
 *   ).create();
 *
 *   // Add acceptable deserialization formats (will still handle object)
 *   // Removes default format. Needs to be added using .format() if still needed.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDate.class,
 *     LocalDateTypeAdapter.builder()
 *       .format("yyyy-MM-dd")
 *       .build()
 *   ).create();
 *
 *   // Serialize as String
 *   // Must add it to accepted formats using .format in order to deserialize.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDate.class,
 *     LocalDateTypeAdapter.builder()
 *       .serializeFormat("yyyy-MM-dd")
 *       .format("yyyy-MM-dd")
 *       .build()
 *   ).create();
 * }</pre>
 */
@Builder
public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

  /**
   * Default format for this TypeAdapter.
   */
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd";

  private static final List<DateTimeFormatter> DEFAULT_FORMAT_STRINGS;

  static {
    List<DateTimeFormatter> dateTimeFormatters = new ArrayList<>();
    dateTimeFormatters.add(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));

    DEFAULT_FORMAT_STRINGS = Collections.unmodifiableList(dateTimeFormatters);
  }

  private static final Gson GSON = new Gson();

  private List<DateTimeFormatter> formats;

  private String serializeFormat;

  /**
   * Helper builder class.
   */
  public static class LocalDateTypeAdapterBuilder {

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
     * @return format
     */
    public final LocalDateTypeAdapterBuilder format(String format) {
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
    public final LocalDateTypeAdapterBuilder serializeFormat(String format) {
      serializeFormat = format;
      return this;
    }
  }

  /**
   * Write to json.
   *
   * @param out output object
   * @param value the Java object to write. May be null.
   * @throws IOException to thrown
   */
  @Override
  public final void write(JsonWriter out, LocalDate value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    if ("OBJECT".equals(serializeFormat)) {
      writeDateObject(out, value);
    } else {
      out.value(value.format(DateTimeFormatter.ofPattern(serializeFormat)));
    }
  }

  /**
   * Read from json.
   *
   * @param in input
   * @return object
   * @throws IOException to thrown
   */
  @Override
  public final LocalDate read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.skipValue();
      return null;
    }

    LocalDate year = readDateObject(in);
    if (year != null) {
      return year;
    }

    String localDateStr = in.nextString();
    for (DateTimeFormatter s : formats.isEmpty() ? DEFAULT_FORMAT_STRINGS : formats) {
      try {
        return LocalDate.parse(localDateStr, s);
      } catch (DateTimeParseException ignored) {
      }
    }

    throw new JsonParseException("Invalid date: " + localDateStr);
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
        throw new SerializationException("Missing year from LocalDate object");
      }
      if (month == null) {
        throw new SerializationException("Missing month from LocalDate object");
      }
      if (day == null) {
        throw new SerializationException("Missing day from LocalDate object");
      }

      return LocalDate.of(year, month, day);
    } catch (IllegalStateException ignored) {
    }

    return null;
  }

  private void writeDateObject(JsonWriter out, LocalDate value) throws IOException {
    out.beginObject();

    out.name("year").value(value.getYear());
    out.name("month").value(value.getMonthValue());
    out.name("day").value(value.getDayOfMonth());

    out.endObject();
  }
}
