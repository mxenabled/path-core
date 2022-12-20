package com.mx.common.configuration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mx.common.lang.Durations;
import com.mx.common.lang.Strings;
import com.mx.common.reflection.Fields;

/**
 * Controls serialization of @Configuration objects.
 * <p>Masks ConfigurationField(secret = true) fields
 * <p><strong>Usage:</strong>
 * <pre>{@code
 * @Data
 * public class ConfigurationWithSecrets {
 *   @ConfigurationField
 *   private String id;
 *
 *   @ConfigurationField(secret = true)
 *   private String password;
 * }
 *
 * ConfigurationWithSecrets configuration = new ConfigurationWithSecrets();
 *
 * configuration.setId("1234");
 * configuration.setPassword("SuperSecret@");
 *
 * Gson gson = new GsonBuilder()
 *   .registerTypeAdapterFactory(new ConfigurationSerializer.Factory())
 *   .create();
 *
 * // Will serialize with the password field masked
 * System.out.println(gson.toJson(configuration));
 *
 * }</pre>
 */
public class ConfigurationSerializer<ST> extends TypeAdapter<ST> {
  private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(new ConfigurationSerializer.Factory()).create();

  private final TypeAdapter<ST> delegate;

  public ConfigurationSerializer(TypeAdapter<ST> delegate) {
    this.delegate = delegate;
  }

  public static class Factory implements TypeAdapterFactory {
    @Override
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

      if (Arrays.stream(type.getRawType().getDeclaredFields()).anyMatch((field) -> field.isAnnotationPresent(ConfigurationField.class))) {
        return new ConfigurationSerializer<T>(delegate);
      }

      return null;
    }
  }

  @Override
  public final void write(JsonWriter out, ST value) throws IOException {
    out.beginObject();
    for (Field field : value.getClass().getDeclaredFields()) {
      ConfigurationField annotation = field.getDeclaredAnnotation(ConfigurationField.class);
      if (annotation != null) {
        writeValue(out, field, value, annotation);
      } // skip fields not annotated with ConfigurationField
    }
    out.endObject();
  }

  @Override
  public final ST read(JsonReader in) throws IOException {
    return delegate.read(in);
  }

  private boolean isSecretList(Field field, ConfigurationField annotation) {
    return List.class.isAssignableFrom(field.getType())
        && annotation.secret()
        && annotation.elementType() == String.class;
  }

  private void writeValue(JsonWriter out, Field field, ST value, ConfigurationField annotation) throws IOException {
    String name = Strings.isNotBlank(annotation.value()) ? annotation.value() : field.getName();
    if (field.getType() == String.class) {
      renderString(out, field, value, annotation, name);
    } else if (isSecretList(field, annotation)) {
      renderSecretList(out, field, value, name);
    } else if (field.getType() == Duration.class) {
      renderDuration(out, field, value, name);
    } else {
      renderOtherNonSecret(out, field, value, annotation, name);
    }
  }

  private void renderDuration(JsonWriter out, Field field, ST value, String name) throws IOException {
    out.name(name).value(Durations.toCompactString((Duration) Fields.getFieldValue(field, value)));
  }

  private void renderOtherNonSecret(JsonWriter out, Field field, ST value, ConfigurationField annotation, String name) throws IOException {
    if (!annotation.secret()) { // Don't use default serialization if marked as secret.
      JsonElement element = GSON.toJsonTree(Fields.getFieldValue(field, value));
      out.name(name);
      GSON.toJson(element, out);
    }
  }

  private void renderSecretList(JsonWriter out, Field field, ST value, String name) throws IOException {
    List<?> list = (List<?>) Fields.getFieldValue(field, value);
    if (list != null) {
      out.name(name).beginArray();
      for (int i = 0; i < list.size(); i++) {
        out.value("****");
      }
      out.endArray();
    }
  }

  private void renderString(JsonWriter out, Field field, ST value, ConfigurationField annotation, String name) throws IOException {
    if (annotation.secret()) {
      String strValue = (String) Fields.getFieldValue(field, value);
      out.name(name).value(strValue == null ? null : Strings.isBlank(strValue) ? "" : "****");
    } else {
      out.name(name).value((String) Fields.getFieldValue(field, value));
    }
  }
}
