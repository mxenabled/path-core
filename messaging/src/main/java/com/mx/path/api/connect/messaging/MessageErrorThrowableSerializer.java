package com.mx.path.api.connect.messaging;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mx.common.messaging.MessageError;
import com.mx.common.messaging.MessageStatus;

public class MessageErrorThrowableSerializer implements JsonDeserializer<Throwable>, JsonSerializer<Throwable> {
  private final Gson gson = new GsonBuilder().create();

  @Override
  public final Throwable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject object = (JsonObject) json;
    String typeName = object.getAsJsonPrimitive("_type").getAsString();
    try {
      Class<?> klass = Class.forName(typeName);
      return (Throwable) gson.fromJson(json, klass);
    } catch (ClassNotFoundException e) {
      throw new MessageError("ClassNotFoundException while parsing a Throwable", MessageStatus.FAIL, e);
    }
  }

  @Override
  public final JsonElement serialize(Throwable src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject element = (JsonObject) gson.toJsonTree(src);
    element.addProperty("_type", src.getClass().getCanonicalName());
    return element;
  }
}
