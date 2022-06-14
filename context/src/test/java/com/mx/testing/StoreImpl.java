package com.mx.testing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import com.mx.common.collections.ObjectMap;
import com.mx.common.store.Store;

public class StoreImpl implements Store {
  Map<String, String> map = new HashMap<>();

  @Getter
  private final ObjectMap configurations;

  public StoreImpl(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public void delete(String key) {
    map.remove(key);
  }

  @Override
  public void deleteSet(String key, String value) {

  }

  @Override
  public String get(String key) {
    return map.get(key);
  }

  @Override
  public Set<String> getSet(String key) {
    return null;
  }

  @Override
  public boolean inSet(String key, String value) {
    return false;
  }

  @Override
  public void put(String key, String value, long expirySeconds) {
    map.put(key, value);
  }

  @Override
  public void putSet(String key, String value, long expirySeconds) {

  }

  @Override
  public void put(String key, String value) {
    map.put(key, value);
  }

  @Override
  public void putSet(String key, String value) {

  }

  @Override
  public boolean putIfNotExist(String key, String value, long expirySeconds) {
    if (map.containsKey(key)) {
      return false;
    }
    put(key, value, 0);
    return true;
  }

  @Override
  public final boolean putIfNotExist(String key, String value) {
    if (map.containsKey(key)) {
      return false;
    }
    put(key, value, 0);
    return true;
  }
}
