package com.mx.path.model.context;

public interface SessionEventListener {
  default void beforeSave(Session session) {
  }
}
