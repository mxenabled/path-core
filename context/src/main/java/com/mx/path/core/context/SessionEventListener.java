package com.mx.path.core.context;

public interface SessionEventListener {
  default void beforeSave(Session session) {
  }
}
