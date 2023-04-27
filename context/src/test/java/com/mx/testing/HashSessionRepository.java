package com.mx.testing;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.serialization.LocalDateTimeDeserializer;
import com.mx.path.core.context.Session;
import com.mx.path.core.context.store.SessionRepository;

/**
 * Stand-in session repository using in-memory HashMap.
 */
public class HashSessionRepository implements SessionRepository {
  public static void register() {
    HashSessionRepository repository = new HashSessionRepository();
    Session.setRepositorySupplier(() -> repository);
  }

  private Map<String, String> values = new HashMap<>();
  private GsonBuilder gsonBuilder = new GsonBuilder();
  private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder()
      .format("yyyy-MM-dd'T'HH:mm:ss.SSS")
      .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
      .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
      .build()).create();

  @Override
  public final void delete(Session session) {
    values.remove(session.getId());
  }

  @Override
  public final void deleteValue(Session session, String key) {
    values.remove(session.getId() + "." + key);
  }

  @Override
  public final String getValue(Session session, String key) {
    return values.get(session.getId() + "." + key);
  }

  @Override
  public final Session load(String sessionId) {
    String json = values.get(sessionId);
    if (Strings.isBlank(json)) {
      return null;
    }
    return GSON.fromJson(json, Session.class);
  }

  @Override
  public final void save(Session session) {
    values.put(session.getId(), GSON.toJson(session));
  }

  @Override
  public final void saveValue(Session session, String key, String value) {
    values.put(session.getId() + "." + key, value);
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value) {
    if (values.containsKey(session.getId() + "." + key)) {
      return false;
    }
    saveValue(session, key, value);

    return true;
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds) {
    return setIfNotExist(session, key, value);
  }

}
