package com.mx.path.gateway.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Creates a singleton Gson serializer compatible with architect responses.
 *
 * todo: This was built specifically for Hughes. We need to look into whether we want to keep this as part of the Path SDK
 *       or whether we want to remote it.
 */
public class Serializer {
  private static Gson gson;

  static {
    gson = new GsonBuilder()
        .setPrettyPrinting()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create();
  }

  public static Gson get() {
    return gson;
  }
}
