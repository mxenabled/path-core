package com.mx.path.core.common.serialization;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

import lombok.Builder;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Used to do Path intra-system (de)serialization of common, complex types.
 */
@Builder
public class SystemTypeAdapterFactory implements TypeAdapterFactory {

  @Builder.Default
  private TypeAdapter<Throwable> throwableTypeAdapter = ThrowableTypeAdapter.builder().build();

  @Builder.Default
  private TypeAdapter<LocalDate> localDateTypeAdapter = LocalDateTypeAdapter.builder().build();

  @Builder.Default
  private TypeAdapter<LocalDateTime> localDateTimeTypeAdapter = LocalDateTimeTypeAdapter.builder().build();

  @SuppressWarnings("unchecked")
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

    if (Throwable.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) throwableTypeAdapter;
    }

    if (LocalDate.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) localDateTypeAdapter;
    }

    if (LocalDateTime.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) localDateTimeTypeAdapter;
    }

    return null;
  }
}
