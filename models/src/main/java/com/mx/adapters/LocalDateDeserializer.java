package com.mx.adapters;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * LocalDate deserializer for use with Gson
 *
 * todo: Move up to models
 */
@Builder
public class LocalDateDeserializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

  private List<DateTimeFormatter> formats;

  public static class LocalDateDeserializerBuilder {

    private List<DateTimeFormatter> formats = new ArrayList<>();

    public final LocalDateDeserializerBuilder format(String format) {
      formats.add(DateTimeFormatter.ofPattern(format));
      return this;
    }

  }

  @Override
  public final LocalDate deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {

    JsonPrimitive dateJson = json.getAsJsonPrimitive();
    if (dateJson.isJsonNull()) {
      return null;
    }
    String localDateStr = dateJson.getAsString();
    for (DateTimeFormatter s : formats) {
      try {
        return LocalDate.parse(localDateStr, s);
      } catch (DateTimeParseException e) {
        continue;
      }
    }

    throw new JsonParseException("Invalid date: " + localDateStr);
  }

  @Override
  public final JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString());
  }

}
