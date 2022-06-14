package com.mx.path.api.connect.messaging;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;

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

  public static class MessageHeadersBuilder {
    private Map<String, String> headers = new LinkedHashMap<>();

    public final MessageHeadersBuilder header(String name, String value) {
      headers.put(name, value);
      return this;
    }
  }

  private Map<String, String> headers = new LinkedHashMap<>();
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
