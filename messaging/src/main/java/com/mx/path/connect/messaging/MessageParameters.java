package com.mx.path.connect.messaging;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;

/**
 * Data class for http request parameters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageParameters {
  private static final Gson GSON = new Gson();

  /**
   * Helper class with fields and methods for {@link MessageParameters}.
   */
  public static class MessageParametersBuilder {
    private Map<String, String> parameters = new LinkedHashMap<>();

    /**
     * Add a parameter to the request with the specified name and value.
     *
     * @param name name of the parameter to be added
     * @param value value associated with parameter
     * @return current {@link MessageParametersBuilder} instance
     */
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

  /**
   * Map of parameters.
   *
   * -- GETTER --
   * Return parameter's map.
   *
   * @return parameter's map
   *
   * -- SETTER --
   * Set parameter's map.
   *
   * @param parameters parameter's map to set
   */
  private Map<String, String> parameters = new LinkedHashMap<>();

  /**
   * Id of request.
   *
   * -- GETTER --
   * Return id value.
   *
   * @return id value
   *
   * -- SETTER --
   * Set id value.
   *
   * @param id id value to set
   */
  private String id;

  /**
   * User id of request.
   *
   * -- GETTER --
   * Return user id value.
   *
   * @return user id value
   *
   * -- SETTER --
   * Set user id value.
   *
   * @param userId user id value to set
   */
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
