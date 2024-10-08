package com.mx.path.core.common.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mx.path.core.common.collection.ObjectArray;
import com.mx.path.core.common.collection.ObjectMap;

/**
 * Gson deserializer for deserializing JSON to ObjectMap/ObjectArray.
 */
public class ObjectMapJsonDeserializer implements JsonDeserializer<ObjectMap> {

  /**
   * Deserialize object.
   *
   * @param json The Json data being deserialized
   * @param typeOfT The type of the Object to deserialize to
   * @param context context
   * @return deserialized object
   * @throws JsonParseException to be thrown
   */
  @Override
  public final ObjectMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return toObjectMap(json);
  }

  private ObjectMap toObjectMap(JsonElement json) {
    JsonObject obj = json.getAsJsonObject();
    ObjectMap result = new ObjectMap();

    obj.entrySet().forEach((entry) -> {
      String name = entry.getKey();
      JsonElement element = entry.getValue();
      if (element.isJsonPrimitive()) {
        result.put(name, toPrimitive(element));
      } else if (element.isJsonObject()) {
        result.put(name, toObjectMap(element));
      } else if (element.isJsonArray()) {
        result.put(name, toObjectArray(element));
      }
    });

    return result;
  }

  private ObjectArray toObjectArray(JsonElement json) {
    JsonArray array = json.getAsJsonArray();
    ObjectArray result = new ObjectArray();

    array.forEach(element -> {
      if (element.isJsonPrimitive()) {
        result.add(toPrimitive(element));
      } else if (element.isJsonObject()) {
        result.add(toObjectMap(element));
      } else if (element.isJsonArray()) {
        result.add(toObjectArray(element));
      }
    });

    return result;
  }

  private Object toPrimitive(JsonElement element) {
    JsonPrimitive primitive = element.getAsJsonPrimitive();

    if (primitive.isBoolean()) {
      return primitive.getAsBoolean();
    } else if (primitive.isNumber()) {
      return primitive.getAsBigDecimal();
    } else if (primitive.isString()) {
      return primitive.getAsString();
    }

    return null;
  }

}
