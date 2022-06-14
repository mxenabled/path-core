package com.mx.testing;

import java.util.Set;

import lombok.Data;

import com.mx.common.collections.ObjectMap;
import com.mx.common.store.Store;

@Data
public class StoreImpl implements Store {

  private ObjectMap configurations;

  public StoreImpl(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public void delete(String key) {

  }

  @Override
  public void deleteSet(String key, String value) {

  }

  @Override
  public String get(String key) {
    return null;
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

  }

  @Override
  public void put(String key, String value) {

  }

  @Override
  public void putSet(String key, String value, long expirySeconds) {

  }

  @Override
  public void putSet(String key, String value) {

  }

  @Override
  public boolean putIfNotExist(String key, String value, long expirySeconds) {
    return false;
  }

  public void describe(ObjectMap description) {
    description.put("class", getClass().getCanonicalName());
    description.put("configurations", configurations);
  }

  @Override
  public boolean putIfNotExist(String key, String value) {
    return false;
  }
}
