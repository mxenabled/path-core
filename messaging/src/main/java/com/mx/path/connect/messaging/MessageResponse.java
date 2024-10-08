package com.mx.path.connect.messaging;

import java.lang.reflect.Type;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.messaging.MessageStatus;
import com.mx.path.core.common.messaging.RemoteException;
import com.mx.path.core.common.serialization.LocalDateDeserializer;
import com.mx.path.core.common.serialization.SystemTypeAdapterFactory;
import com.mx.path.core.common.serialization.ThrowableTypeAdapter;

/**
 * Request message response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
  private static GsonBuilder gsonBuilder = new GsonBuilder();
  private static Gson gson = gsonBuilder
      .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder().build())
      .registerTypeAdapterFactory(
          SystemTypeAdapterFactory
              .builder()
              .throwableTypeAdapter(ThrowableTypeAdapter.builder().fallbackType(RemoteException.class).build())
              .build())
      .create();

  @SuppressWarnings("checkstyle:HiddenField")
  public static class MessageResponseBuilder {
    private String body;

    /**
     * -- GETTER --
     * Set new value on body.
     *
     * @param body body to set
     * @return self
     */
    public final MessageResponseBuilder body(String body) {
      this.body = body;
      return this;
    }

    /**
     * -- GETTER --
     * Set new value on body.
     *
     * @param body body to set
     * @return self
     */
    public final MessageResponseBuilder body(Object body) {
      this.body = gson.toJson(body);
      return this;
    }
  }

  /**
   * @param json representation of a MessageRequest object
   * @return deserialized object
   */
  public static MessageResponse fromJson(String json) {
    return gson.fromJson(json, MessageResponse.class);
  }

  private String body;
  private String error;
  private MessageHeaders messageHeaders;
  private MessageStatus status;
  private Throwable exception;
  private MessageRequest request;

  /**
   * Sets body. Can be a String, Primitive or object.
   * @param body as String
   */
  public final void setBody(String body) {
    this.body = body;
  }

  /**
   * Sets body from Object
   *
   * Extract using getBodyAs(Class)
   * @param body as Object
   */
  public final void setBody(Object body) {
    this.body = gson.toJson(body);
  }

  /**
   * Get body deserialized as classOfT.
   *
   * @param <T> type of object to deserialize into
   * @param classOfT class of object to deserialize into
   * @return deserialized object of type T
   */
  public final <T> T getBodyAs(Class<T> classOfT) {
    return gson.fromJson(body, classOfT);
  }

  /**
   * Get body deserialized as typeOfT.
   *
   * @param <T> type of object to deserialize into
   * @param typeOfT type of the object to deserialize into
   * @return deserialized object of type T
   */
  public final <T> T getBodyAs(Type typeOfT) {
    return gson.fromJson(body, typeOfT);
  }

  /**
   * @return Json representation of this
   */
  public final String toJson() {
    return gson.toJson(this);
  }
}
