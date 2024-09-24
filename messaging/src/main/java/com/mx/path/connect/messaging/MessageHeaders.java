package com.mx.path.connect.messaging;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;

/**
 * Data class for http requests headers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeaders {
  private static final Gson GSON = new Gson();

  /**
   * @param json representation of a MessageHeaders object
   * @return deserialized object
   */
  public static MessageHeaders fromJson(String json) {
    return GSON.fromJson(json, MessageHeaders.class);
  }

  /**
   * Helper class with fields and methods for {@link MessageHeaders}.
   */
  public static class MessageHeadersBuilder {
    private Map<String, String> headers = new LinkedHashMap<>();

    /**
     * Build a new {@link MessageHeadersBuilder} instance and append a new header with the specified name and value.
     *
     * @param name name of appended header
     * @param value value of appended header
     * @return new instance of {@link MessageHeadersBuilder}
     */
    public final MessageHeadersBuilder header(String name, String value) {
      headers.put(name, value);
      return this;
    }
  }

  /**
   * -- GETTER --
   * Return headers map.
   *
   * @return headers map
   *
   * -- SETTER --
   * Set headers map.
   *
   * @param headers headers map to set
   */
  private Map<String, String> headers = new LinkedHashMap<>();

  /**
   * -- GETTER --
   * Return session id.
   *
   * @return session id
   *
   * -- SETTER --
   * Set session id.
   *
   * @param sessionId session id to set
   */
  private String sessionId;

  /**
   * @param name of header
   * @return value of header
   */
  public final String get(String name) {
    return headers.get(name);
  }

  /**
   * @param name of header
   * @param value of header
   */
  public final void put(String name, String value) {
    headers.put(name, value);
  }

  /**
   * @return Json representation of this
   */
  public final String toJson() {
    return GSON.toJson(this);
  }
}
