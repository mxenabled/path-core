package com.mx.path.core.context;

/**
 * Interface for session event listener.
 */
public interface SessionEventListener {

  /**
   * Action to execute before session is saved.
   *
   * @param session session associated with event listener
   */
  default void beforeSave(Session session) {
  }
}
