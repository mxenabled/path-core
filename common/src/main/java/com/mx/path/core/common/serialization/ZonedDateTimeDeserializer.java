package com.mx.path.core.common.serialization;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mx.path.core.common.lang.Strings;

/**
 * ZonedDateTime deserializer for use with Gson
 *
 * <p>Default Behavior:
 *
 * <ul>
 *   <li>Serialize ZonedDateTime to Object</li>
 *   <li>Allow deserialization of ZonedDateTime from object Object</li>
 *   <li>Allow deserialization of ZonedDateTime from from string format "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"</li>
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
public class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  private static final List<DateTimeFormatter> DEFAULT_FORMAT_STRINGS;
  static {
    List<DateTimeFormatter> dateTimeFormatters = new ArrayList<>();
    dateTimeFormatters.add(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));

    DEFAULT_FORMAT_STRINGS = Collections.unmodifiableList(dateTimeFormatters);
  }

  private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(ZoneId.class, new ZoneIdTypeAdapter())
      .registerTypeAdapter(ZoneOffset.class, new ZoneOffsetTypeAdapter())
      .create();

  private List<DateTimeFormatter> formats;

  private String serializeFormat;

  public static class ZonedDateTimeDeserializerBuilder {

    private List<DateTimeFormatter> formats = new ArrayList<>();

    private String serializeFormat = "OBJECT";

    /**
     * Provide a ZonedDateTime string format that is accepted
     *
     * <p>The formats need to be provided according to {@link DateTimeFormatter} specs.
     *
     * @param format Format string of acceptable DateTime format
     * @return builder
     */
    public final ZonedDateTimeDeserializerBuilder format(String format) {
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
    public final ZonedDateTimeDeserializerBuilder serializeFormat(String format) {
      serializeFormat = format;
      return this;
    }

  }

  @Override
  public final ZonedDateTime deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {

    if (json.isJsonObject()) {
      ZonedDateTimeJava8 zonedDateTimeJava8 = GSON.fromJson(json, ZonedDateTimeJava8.class);
      return zonedDateTimeJava8.toZonedDateTime();
    }

    JsonPrimitive dateJson = json.getAsJsonPrimitive();
    if (dateJson.isJsonNull()) {
      return null;
    }
    String zonedDateTimeStr = dateJson.getAsString();
    for (DateTimeFormatter s : formats.isEmpty() ? DEFAULT_FORMAT_STRINGS : formats) {
      try {
        return ZonedDateTime.parse(zonedDateTimeStr, s);
      } catch (DateTimeParseException e) {
        continue;
      }
    }

    throw new JsonParseException("Invalid dateTime: " + zonedDateTimeStr);
  }

  @Override
  public final JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
    if (Strings.isBlank(serializeFormat) || serializeFormat.equals("OBJECT")) {
      return GSON.toJsonTree(new ZonedDateTimeJava8(src));
    } else {
      return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(serializeFormat)));
    }
  }
}
