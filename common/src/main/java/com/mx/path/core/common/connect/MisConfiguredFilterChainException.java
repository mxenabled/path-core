package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.AccessorSystemException;

/**
 * Thrown when a request is being constructed without a null filterChain.
 */
public final class MisConfiguredFilterChainException extends AccessorSystemException {

  /**
   * Default constructor.
   */
  public MisConfiguredFilterChainException() {
    super("Mis-configured Request. filterChain is null");
  }
}
