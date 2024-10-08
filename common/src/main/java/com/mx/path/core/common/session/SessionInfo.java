package com.mx.path.core.common.session;

/**
 * Interface for session info.
 */
public interface SessionInfo {
  /**
   * @return unique session identifier
   */
  String getSessionId();

  /**
   * @return number of seconds until the session expires
   */
  long getExpiresIn();
}
