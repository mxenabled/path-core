package com.mx.path.connect.messaging;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.serialization.LocalDateDeserializer;

/**
 * Data class for http requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest implements Message {
  private static GsonBuilder gsonBuilder = new GsonBuilder();
  private static Gson gson = gsonBuilder.registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder()
      .build()).create();

  /**
   * Constant to convert nanoseconds to milliseconds, i.e. 1 millisecond = 1000000 nanoseconds.
   */
  public static final Integer NANO_TO_MILLISECONDS = 1000000;

  /**
   * Helper class with fields and methods for {@link MessageRequest}.
   */
  @SuppressWarnings("checkstyle:HiddenField")
  public static class MessageRequestBuilder {
    private String body;

    /**
     * Set body value and return self.
     *
     * @param body body value to be set
     * @return current {@link MessageRequestBuilder} instance
     */
    public final MessageRequestBuilder body(String body) {
      this.body = body;
      return this;
    }

    /**
     * Serialize object and set serialized value on body.
     *
     * @param body body value to be serialized and set
     * @return current {@link MessageRequestBuilder} instance
     */
    public final MessageRequestBuilder body(Object body) {
      this.body = gson.toJson(body);
      return this;
    }
  }

  /**
   * @param json representation of a MessageRequest object
   * @return deserialized object
   */
  public static MessageRequest fromJson(String json) {
    return gson.fromJson(json, MessageRequest.class);
  }

  /**
   * Body of request.
   *
   * -- GETTER --
   * Return body value.
   *
   * @return body value
   *
   * -- SETTER --
   * Set body value.
   *
   * @param body value to set
   */
  private String body;

  /**
   * Channel of request.
   *
   * -- GETTER --
   * Return channel value.
   *
   * @return channel value
   *
   * -- SETTER --
   * Set channel value.
   *
   * @param channel value to set
   */
  private transient String channel;

  /**
   * Headers of request.
   *
   * -- GETTER --
   * Return headers value.
   *
   * @return headers value
   *
   * -- SETTER --
   * Set headers value.
   *
   * @param messageHeaders value to set
   */
  private MessageHeaders messageHeaders;

  /**
   * Parameters of request.
   *
   * -- GETTER --
   * Return parameters value.
   *
   * @return parameters value
   *
   * -- SETTER --
   * Set parameters value.
   *
   * @param messageParameters value to set
   */
  private MessageParameters messageParameters;

  /**
   * Model of request.
   *
   * -- GETTER --
   * Return model value.
   *
   * @return model value
   *
   * -- SETTER --
   * Set model value.
   *
   * @param model value to set
   */
  private String model;

  /**
   * Operation of request.
   *
   * -- GETTER --
   * Return operation value.
   *
   * @return operation value
   *
   * -- SETTER --
   * Set operation value.
   *
   * @param operation value to set
   */
  private String operation;

  /**
   * Time of Java Virtual Machine's high-resolution time source at start of operation.
   *
   * -- GETTER --
   * Return startNano value.
   *
   * @return startNano value
   *
   * -- SETTER --
   * Set startNano value.
   *
   * @param startNano value to set
   */
  private Long startNano;

  /**
   * Time of Java Virtual Machine's high-resolution time source at end of operation.
   *
   * -- GETTER --
   * Return endNano value.
   *
   * @return endNano value
   *
   * -- SETTER --
   * Set endNano value.
   *
   * @param endNano value to set
   */
  private Long endNano;

  /**
   * Sets body. Can be a String, Primitive or object.
   * @param body as String
   */
  public final void setBody(String body) {
    this.body = body;
  }

  /**
   * Sets body from Object.
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
   * Return operation duration on milliseconds.
   *
   * @return operation duration on milliseconds
   */
  public final long getDuration() {
    if (startNano == null) {
      return 0;
    }

    if (this.endNano == null) {
      finish();
    }

    return (endNano - startNano) / NANO_TO_MILLISECONDS;
  }

  /**
   * Stops the timer on this request
   */
  public final void finish() {
    this.endNano = System.nanoTime();
  }

  /**
   * @return Json representation of this
   */
  public final String toJson() {
    return gson.toJson(this);
  }

  /**
   * Sets start time
   * @return this MessageRequest
   */
  public final MessageRequest start() {
    this.startNano = System.nanoTime();

    return this;
  }
}
