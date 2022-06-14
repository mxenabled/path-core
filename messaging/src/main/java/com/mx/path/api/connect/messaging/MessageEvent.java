package com.mx.path.api.connect.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;

/**
 * Represents an event to emit to a channel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent implements Message {
  private static final Gson GSON = new Gson();

  @SuppressWarnings("checkstyle:HiddenField")
  public static class MessageEventBuilder {
    private String body;

    public final MessageEventBuilder body(String body) {
      this.body = body;
      return this;
    }

    public final MessageEventBuilder body(Object body) {
      this.body = GSON.toJson(body);
      return this;
    }
  }

  /**
   * @param json representation of a MessageRequest object
   * @return deserialized object
   */
  public static MessageEvent fromJson(String json) {
    return GSON.fromJson(json, MessageEvent.class);
  }

  private String body;
  private transient String channel;
  private String event;
  private MessageHeaders messageHeaders;
  private MessageParameters messageParameters;

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
    this.body = GSON.toJson(body);
  }

  /**
   * Get body deserialized as classOfT
   * @param classOfT
   * @return body cast to classOfT
   */
  public final <T> T getBodyAs(Class<T> classOfT) {
    return GSON.fromJson(body, classOfT);
  }

  /**
   * @return Json representation of this
   */
  public final String toJson() {
    return GSON.toJson(this);
  }
}
