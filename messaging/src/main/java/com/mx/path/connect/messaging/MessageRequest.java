package com.mx.path.connect.messaging;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.serialization.LocalDateDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest implements Message {
  private static GsonBuilder gsonBuilder = new GsonBuilder();
  private static Gson gson = gsonBuilder.registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder()
      .build()).create();
  public static final Integer NANO_TO_SECONDS = 1000000;

  @SuppressWarnings("checkstyle:HiddenField")
  public static class MessageRequestBuilder {
    private String body;

    public final MessageRequestBuilder body(String body) {
      this.body = body;
      return this;
    }

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

  private String body;
  private transient String channel;
  private MessageHeaders messageHeaders;
  private MessageParameters messageParameters;
  private String model;
  private String operation;
  private Long startNano;
  private Long endNano;

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
   * Get body deserialized as classOfT
   * @param classOfT
   * @return body cast to classOfT
   */
  public final <T> T getBodyAs(Class<T> classOfT) {
    return gson.fromJson(body, classOfT);
  }

  public final long getDuration() {
    if (startNano == null) {
      return 0;
    }

    if (this.endNano == null) {
      finish();
    }

    return (endNano - startNano) / NANO_TO_SECONDS;
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
