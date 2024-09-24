package com.mx.path.gateway.configuration;

import com.mx.path.gateway.accessor.Accessor;

/**
 * Interface for accessor proxy.
 */
public interface AccessorProxy {

  /**
   * Return accessor class.
   *
   * @return accessor class
   */
  Class<? extends Accessor> getAccessorClass();

  /**
   * Return scope.
   *
   * @return scope
   */
  String getScope();

  /**
   * Build accessor.
   *
   * @param <T> accessor type
   * @return accessor
   */
  <T extends Accessor> T build();
}
