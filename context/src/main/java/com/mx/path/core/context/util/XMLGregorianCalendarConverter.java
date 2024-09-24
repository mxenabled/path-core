package com.mx.path.core.context.util;

import java.lang.reflect.Type;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Utility class to serialize and deserialize {@link XMLGregorianCalendar}.
 */
public class XMLGregorianCalendarConverter {

  /**
   * Implements JsonSerializer for XMLGregorianCalendar.
   */
  public static class Serializer implements JsonSerializer<XMLGregorianCalendar> {

    /**
     * Serialize calendar.
     *
     * @param xmlGregorianCalendar the object that needs to be converted to Json.
     * @param type the actual type (fully genericized version) of the source object.
     * @param jsonSerializationContext json serializer context
     * @return serialized object
     */
    @Override
    public final JsonElement serialize(XMLGregorianCalendar xmlGregorianCalendar, Type type, JsonSerializationContext jsonSerializationContext) {
      return new JsonPrimitive(xmlGregorianCalendar.toXMLFormat());
    }
  }

  /**
   * Implements JsonDeserializer for XMLGregorianCalendar.
   */
  public static class Deserializer implements JsonDeserializer<XMLGregorianCalendar> {

    /**
     * Deserialize calendar.
     *
     * @param jsonElement The Json data being deserialized
     * @param type The type of the Object to deserialize to
     * @param jsonDeserializationContext json deserializer context
     * @return deserialized object
     */
    @Override
    public final XMLGregorianCalendar deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) {
      try {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(jsonElement.getAsString());
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  }
}
