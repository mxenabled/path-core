package com.mx.path.core.common.serialization;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mx.path.core.common.lang.Strings;

/**
 * LocalDateTime deserializer for use with Gson
 *
 * <p>Default Behavior:
 *
 * <ul>
 *   <li>Serialize LocalDateTime to Object</li>
 *   <li>Allow deserialization of LocalDateTime from object Object</li>
 *   <li>Allow deserialization of LocalDateTime from from string format "yyyy-MM-dd'T'HH:mm:ss.SSS"</li>
 * </ul>
 *
 * <p>Examples:
 *
 * <pre>{@code
 *   // Take default behavior
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDateTime.class,
 *     LocalDateTimeDeserializer.builder().build()
 *   ).create();
 *
 *   // Add acceptable deserialization formats (will still handle object)
 *   // Removes default format. Needs to be added using .format() if still needed.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDateTime.class,
 *     LocalDateTimeDeserializer.builder()
 *       .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
 *       .build()
 *   ).create();
 *
 *   // Serialize as String
 *   // Must add it to accepted formats using .format in order to deserialize.
 *   Gson gson = new GsonBuilder().registerTypeAdapter(
 *     LocalDateTime.class,
 *     LocalDateTimeDeserializer.builder()
 *       .serializeFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
 *       .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
 *       .build()
 *   ).create();
 * }</pre>
 *
 * @deprecated Use {@link LocalDateTimeTypeAdapter}
 */
@Deprecated
@Builder
public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
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

  public static class LocalDateTimeDeserializerBuilder {

    private List<DateTimeFormatter> formats = new ArrayList<>();

    private String serializeFormat = "OBJECT";

    /**
     * Provide a LocalDateTime string format that is accepted
     *
     * <p>The formats need to be provided according to {@link DateTimeFormatter} specs.
     *
     * @param format Format string of acceptable DateTime format
     * @return builder
     */
    public final LocalDateTimeDeserializerBuilder format(String format) {
      formats.add(DateTimeFormatter.ofPattern(format));
      return this;
    }

    /**
     * Provide format for serializing a LocalDateTime object to JSON
     *
     * @param format A DateTimeFormatter format String or
     *               OBJECT to serialize as an object (default)
     * @return builder
     */
    public final LocalDateTimeDeserializerBuilder serializeFormat(String format) {
      serializeFormat = format;
      return this;
    }

  }

  @Override
  public final LocalDateTime deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {

    if (json.isJsonObject()) {
      LocalDateTimeJava8 localDateTimeJava8 = GSON.fromJson(json, LocalDateTimeJava8.class);
      return localDateTimeJava8.toLocalDateTime();
    }

    JsonPrimitive dateJson = json.getAsJsonPrimitive();
    if (dateJson.isJsonNull()) {
      return null;
    }
    String localDateTimeStr = dateJson.getAsString();
    for (DateTimeFormatter s : formats.isEmpty() ? DEFAULT_FORMAT_STRINGS : formats) {
      try {
        return LocalDateTime.parse(localDateTimeStr, s);
      } catch (DateTimeParseException e) {
        continue;
      }
    }

    throw new JsonParseException("Invalid dateTime: " + localDateTimeStr);
  }

  @Override
  public final JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
    if (Strings.isBlank(serializeFormat) || serializeFormat.equals("OBJECT")) {
      return GSON.toJsonTree(new LocalDateTimeJava8(src));
    } else {
      return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(serializeFormat)));
    }
  }
}
