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

public class XMLGregorianCalendarConverter {

  public static class Serializer implements JsonSerializer<XMLGregorianCalendar> {

    @Override
    public final JsonElement serialize(XMLGregorianCalendar xmlGregorianCalendar, Type type, JsonSerializationContext jsonSerializationContext) {
      return new JsonPrimitive(xmlGregorianCalendar.toXMLFormat());
    }
  }

  public static class Deserializer implements JsonDeserializer<XMLGregorianCalendar> {

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
