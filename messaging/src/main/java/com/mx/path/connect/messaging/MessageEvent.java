package com.mx.path.connect.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;

/**
 * Represents an event to emit to a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent implements Message {
  private static final Gson GSON = new Gson();

  /**
   * Utility class with fields and methods for {@link MessageEvent}.
   */
  @SuppressWarnings("checkstyle:HiddenField")
  public static class MessageEventBuilder {

    /**
     * -- GETTER --
     * Return event body.
     *
     * @return event body
     *
     * -- SETTER --
     * Set event body.
     *
     * @param body body to set
     */
    private String body;

    /**
     * Set body value and return self.
     *
     * @param body body value to be set
     * @return current {@link MessageEvent.MessageEventBuilder} instance
     */
    public final MessageEventBuilder body(String body) {
      this.body = body;
      return this;
    }

    /**
     * Serialize object and set serialized value on body.
     *
     * @param body body value to be serialized and set
     * @return current {@link MessageEvent.MessageEventBuilder} instance
     */
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

  /**
   * -- GETTER --
   * Return event body.
   *
   * @return event body
   *
   * -- SETTER --
   * Set event body.
   *
   * @param body body to set
   */
  private String body;

  /**
   * -- GETTER --
   * Return event channel.
   *
   * @return event channel
   *
   * -- SETTER --
   * Set event channel.
   *
   * @param channel channel to set
   */
  private transient String channel;

  /**
   * -- GETTER --
   * Return event name.
   *
   * @return event name
   *
   * -- SETTER --
   * Set event name.
   *
   * @param event event name to set
   */
  private String event;

  /**
   * -- GETTER --
   * Return event headers.
   *
   * @return event headers
   *
   * -- SETTER --
   * Set event headers.
   *
   * @param messageHeaders event headers to set
   */
  private MessageHeaders messageHeaders;

  /**
   * -- GETTER --
   * Return event parameters.
   *
   * @return event parameters
   *
   * -- SETTER --
   * Set event parameters.
   *
   * @param messageParameters event parameters to set
   */
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
   * Get body deserialized as classOfT.
   *
   * @param <T> type of object to deserialize into
   * @param classOfT class of object to deserialize into
   * @return deserialized object of type T
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
