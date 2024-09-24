package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;
import com.mx.path.core.common.exception.PathSystemException;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.reflection.Constructors;
import com.mx.path.core.common.reflection.Fields;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 * Handles intra-system Throwable (de)serialization.
 *
 * <p>Attempt is made to reinflate the same exception type on deserialization, but has the ability to fall back to a
 * defined Throwable type. This is accomplished by including the actual throwable type -- `_type` and an optional
 * fallback type `_fallbackType` in the serialized payload. The adapter can also be instantiated with an other
 * fallback type ({@link #fallbackType}).
 *
 * <p>If the Throwable is a descendant of {@link PathRequestException}, `_fallbackType` will be set to PathRequestException
 * <p>If the Throwable is a descendant of {@link PathSystemException}, `_fallbackType` will be set to PathSystemException
 * <p>By default, `_fallbackType` will be set to {@link RuntimeException}
 */
@Builder
public class ThrowableTypeAdapter extends TypeAdapter<Throwable> {
  @Builder.Default
  private Class<? extends Throwable> fallbackType = RuntimeException.class;

  /**
   * Write to json output.
   *
   * @param out output
   * @param value the Java object to write. May be null.
   * @throws IOException to be thrown
   */
  @Override
  @SuppressFBWarnings("BC_UNCONFIRMED_CAST")
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public final void write(JsonWriter out, Throwable value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    out.beginObject();

    out.name("_type").value(value.getClass().getCanonicalName());
    out.name("message").value(value.getMessage());
    if (value.getCause() != null) {
      write(out.name("cause"), value.getCause());
    }

    if (PathSystemException.class.isAssignableFrom(value.getClass())) {
      out.name("_fallbackType").value(PathSystemSerializableException.class.getCanonicalName());
    } else if (PathRequestException.class.isAssignableFrom(value.getClass())) {
      PathRequestException pathRequestException = (PathRequestException) value;
      out.name("report").value(pathRequestException.shouldReport());
      out.name("_fallbackType").value(PathRequestSerializableException.class.getCanonicalName());
      if (Strings.isNotBlank(pathRequestException.getUserMessage())) {
        out.name("userMessage").value(pathRequestException.getUserMessage());
      }
      if (Strings.isNotBlank(pathRequestException.getReason())) {
        out.name("reason").value(pathRequestException.getReason());
      }
      if (pathRequestException.getStatus() != null) {
        out.name("status").value(pathRequestException.getStatus().name());
      }
      if (pathRequestException.getCode() != null) {
        out.name("code").value(pathRequestException.getCode());
      }
      if (pathRequestException.getErrorTitle() != null) {
        out.name("errorTitle").value(pathRequestException.getErrorTitle());
      }
    } else {
      out.name("_fallbackType").value(RuntimeException.class.getCanonicalName());
    }

    out.endObject();
  }

  @Data
  private static class ErrorInfo {
    private String throwableType = null;
    private String payloadFallbackType = null;
    private Throwable cause = null;
    private String code = null;
    private String errorTitle = null;
    private String message = null;
    private String reason = null;
    private Boolean report = null;
    private PathResponseStatus status = null;
    private String userMessage = null;

    // todo: handle headers?
  }

  /**
   * Read from json.
   *
   * @param in input
   * @return object
   * @throws IOException to be thrown
   */
  @SuppressWarnings({ "unchecked", "PMD.CyclomaticComplexity" })
  @Override
  public final Throwable read(JsonReader in) throws IOException {

    final ErrorInfo errorInfo = new ErrorInfo();

    in.beginObject();
    while (in.hasNext()) {
      String name = in.nextName();
      if (name.equals("_type")) {
        errorInfo.throwableType = in.nextString();
      } else if (name.equals("_fallbackType")) {
        errorInfo.payloadFallbackType = in.nextString();
      } else if (name.equals("cause")) {
        errorInfo.cause = read(in);
      } else if (name.equals("code")) {
        errorInfo.code = in.nextString();
      } else if (name.equals("errorTitle")) {
        errorInfo.errorTitle = in.nextString();
      } else if (name.equals("message")) {
        errorInfo.message = in.nextString();
      } else if (name.equals("reason")) {
        errorInfo.reason = in.nextString();
      } else if (name.equals("report")) {
        errorInfo.report = in.nextBoolean();
      } else if (name.equals("status")) {
        errorInfo.status = PathResponseStatus.valueOf(in.nextString());
      } else if (name.equals("userMessage")) {
        errorInfo.userMessage = in.nextString();
      } else {
        in.skipValue();
      }
    }
    in.endObject();

    if (Strings.isBlank(errorInfo.throwableType)) {
      return null;
    }

    List<Object> l = new ArrayList<>();
    l.add(errorInfo.throwableType);
    if (Strings.isNotBlank(errorInfo.payloadFallbackType)) {
      l.add(errorInfo.payloadFallbackType);
    }
    l.add(fallbackType);

    Throwable result = l.stream().map((t) -> buildInstance(t, errorInfo))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);

    if (result == null) {
      throw new SerializationException("Unable to deserialize throwable: " + errorInfo.throwableType);
    }

    Class<Throwable> type = (Class<Throwable>) result.getClass();

    setField(type, result, Throwable.class, errorInfo.cause, "cause", "setCause");
    setField(type, result, String.class, errorInfo.code, "code", "setCode");
    setField(type, result, String.class, errorInfo.errorTitle, "errorTitle", "setErrorTitle");
    setField(type, result, String.class, errorInfo.message, "message", "setMessage");
    setField(type, result, String.class, errorInfo.reason, "reason", "setReason");
    setField(type, result, boolean.class, errorInfo.report, "report", "setReport");
    setField(type, result, PathResponseStatus.class, errorInfo.status, "status", "setStatus");
    setField(type, result, String.class, errorInfo.userMessage, "userMessage", "setUserMessage");
    setField(type, result, String.class, errorInfo.getThrowableType(), "originalType", "setOriginalType");

    return result;
  }

  /**
   * Safely attempt to create an instance of given type or type name
   *
   * <p>Notes: Unless overridden, fields in basic exception types cannot be written to after constructions.
   * This code will attempt to create the Throwable using common constructors. If that fails, an instance will be created
   * using {@link ObjenesisStd}. After construction, an attempt will be made to write all common values to the new
   * instance. These include all fields contained in {@link ErrorInfo}.
   *
   * @param typeOrTypeName A Class or String
   * @return new instance or null
   */
  @SuppressWarnings("unchecked")
  private Throwable buildInstance(Object typeOrTypeName, ErrorInfo errorInfo) {
    Class<? extends Throwable> type = getType(typeOrTypeName);

    if (type == null) {
      return null;
    }

    /**
     * Attempt to build using common constructors
     */
    Throwable instance = buildFromConstructor(type, errorInfo);
    if (instance != null) {
      return instance;
    }

    // Use Objenesis to bring an instance into existence
    try {
      Objenesis objenesis = new ObjenesisStd();
      ObjectInstantiator inst = objenesis.getInstantiatorOf(type);
      return (Throwable) inst.newInstance();
    } catch (RuntimeException e) {
      // This could fail because the class is abstract
      return null;
    }
  }

  private <T extends Throwable> T buildFromConstructor(Class<T> type, ErrorInfo errorInfo) {
    // todo: need to make this smarter
    Constructor<T> constructor = Constructors.safeGetConstructor(type, String.class, String.class, Throwable.class);
    T instance = Constructors.safeInstantiate(constructor, errorInfo.message, errorInfo.userMessage, (Throwable) errorInfo.cause);
    if (instance != null) {
      return instance;
    }

    constructor = Constructors.safeGetConstructor(type, String.class, Throwable.class);
    instance = Constructors.safeInstantiate(constructor, errorInfo.message, errorInfo.cause);
    if (instance != null) {
      return instance;
    }

    constructor = Constructors.safeGetConstructor(type, String.class);
    instance = Constructors.safeInstantiate(constructor, errorInfo.message);
    if (instance != null) {
      return instance;
    }

    constructor = Constructors.safeGetConstructor(type, Throwable.class);
    instance = Constructors.safeInstantiate(constructor, errorInfo.cause);
    if (instance != null) {
      return instance;
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Throwable> getType(Object typeOrTypeName) {
    if (typeOrTypeName instanceof String) {
      try {
        return (Class<Throwable>) Class.forName(typeOrTypeName.toString());
      } catch (ClassNotFoundException ignored) {
        return null;
      }
    } else {
      return (Class<Throwable>) typeOrTypeName;
    }
  }

  private <T> void setField(Class<Throwable> type, Throwable result, Class<T> typeOfField, T value, String fieldName, String setterName) {
    if (value == null) {
      return;
    }

    try {
      Field messageField = type.getField(fieldName);
      Fields.setFieldValue(messageField, result, value);
    } catch (NoSuchFieldException e) {
      try {
        Method messageSetter = type.getMethod(setterName, typeOfField);
        messageSetter.invoke(result, value);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        // NO OP
      }
    }
  }
}
