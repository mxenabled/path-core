package com.mx.common.session;

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
