package com.mx.models;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MdxLocalDateSerializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

  @Override
  public final LocalDate deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {

    LocalDate localDate = null;
    JsonPrimitive dateJson = json.getAsJsonPrimitive();
    if (dateJson.isJsonNull()) {
      return null;
    }
    String localDateStr = dateJson.getAsString();
    try {
      localDate = LocalDate.parse(localDateStr);
    } catch (DateTimeParseException e) {
      throw new JsonParseException("Invalid date");
    }
    return localDate;
  }

  @Override
  public final JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.toString());
  }

}
