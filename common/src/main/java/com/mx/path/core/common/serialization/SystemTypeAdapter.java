package com.mx.path.core.common.serialization;

import java.time.Duration;
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
public class SystemTypeAdapter implements TypeAdapterFactory {

  @Builder.Default
  private TypeAdapter<Throwable> throwableTypeAdapter = ThrowableTypeAdapter.builder().build();

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

    return null;
  }
}
