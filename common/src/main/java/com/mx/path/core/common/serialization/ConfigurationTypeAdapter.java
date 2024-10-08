package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.reflection.Fields;

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
public class ConfigurationTypeAdapter<ST> extends TypeAdapter<ST> {
  private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(new ConfigurationTypeAdapter.Factory()).create();

  private final TypeAdapter<ST> delegate;

  /**
   * Build new instance of {@link ConfigurationTypeAdapter}.
   *
   * @param delegate delegated type adapter
   */
  public ConfigurationTypeAdapter(TypeAdapter<ST> delegate) {
    this.delegate = delegate;
  }

  /**
   * Auxiliary class to build new type adapters.
   */
  @SuppressWarnings("unchecked")
  public static class Factory implements TypeAdapterFactory {

    /**
     * Create new instance of {@link TypeAdapter} with type T.
     *
     * @param gson class json data
     * @param type type of class
     * @return new instance of T
     * @param <T> type to return
     */
    @Override
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      if (type.getRawType() == Duration.class) {
        return (TypeAdapter<T>) new DurationTypeAdapter();
      }
      if (type.getRawType() == Pattern.class) {
        return (TypeAdapter<T>) new PatternTypeAdapter();
      }
      if (ZoneId.class.isAssignableFrom(type.getRawType())) {
        return (TypeAdapter<T>) new ZoneIdTypeAdapter();
      }

      TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

      if (Arrays.stream(type.getRawType().getDeclaredFields()).anyMatch((field) -> field.isAnnotationPresent(ConfigurationField.class))) {
        return new ConfigurationTypeAdapter<T>(delegate);
      }

      return null;
    }
  }

  /**
   * Write value to output.
   *
   * @param out write output
   * @param value the Java object to write. May be null.
   * @throws IOException to be thrown
   */
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

  /**
   * Read valur from input.
   *
   * @param in input
   * @return value
   * @throws IOException to thrown
   */
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
    } else {
      renderOtherNonSecret(out, field, value, annotation, name);
    }
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
