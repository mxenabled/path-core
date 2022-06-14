package com.mx.adapters;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
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
 * Date deserializer for use with Gson will be doing the UTC format Date
 *
 *
 */
@Builder
public class DateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {

  private List<DateTimeFormatter> formats;

  public static class DateDeserializerBuilder {

    private List<DateTimeFormatter> formats = new ArrayList<>();

    public final DateDeserializerBuilder format(String format) {
      formats.add(DateTimeFormatter.ofPattern(format));
      return this;
    }

  }

  @Override
  public final Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {

    JsonPrimitive dateJson = json.getAsJsonPrimitive();
    if (dateJson.isJsonNull()) {
      return null;
    }
    String localDateStr = dateJson.getAsString();
    for (DateTimeFormatter s : formats) {
      try {
        LocalDateTime localDate = LocalDateTime.parse(localDateStr, s);
        return Date.from(localDate.atZone(ZoneOffset.UTC).toInstant());
      } catch (DateTimeParseException e) {
        continue;
      }
    }

    throw new JsonParseException("Invalid date: " + localDateStr);
  }

  @Override
  public final JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString());
  }

}
