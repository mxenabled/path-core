package com.mx.path.connect.messaging;

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
public class MessageParameters {
  private static final Gson GSON = new Gson();

  public static class MessageParametersBuilder {
    private Map<String, String> parameters = new LinkedHashMap<>();

    public final MessageParametersBuilder parameter(String name, String value) {
      parameters.put(name, value);
      return this;
    }
  }

  /**
   * @param json representation of a MessageParameters object
   * @return deserialized object
   */
  public static MessageParameters fromJson(String json) {
    return GSON.fromJson(json, MessageParameters.class);
  }

  private Map<String, String> parameters = new LinkedHashMap<>();
  private String id;
  private String userId;

  /**
   * @param name of parameter
   * @param value of parameter
   */
  public final void put(String name, String value) {
    parameters.put(name, value);
  }

  /**
   * @param name of parameter
   * @return value of parameter
   */
  public final String get(String name) {
    return parameters.get(name);
  }

  /**
   * @return Json representation of this
   */
  public final String toJson() {
    return GSON.toJson(this);
  }
}
