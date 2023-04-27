package com.mx.path.core.common.model;

/**
 * Interface for wrappable objects. Used to determine if a JSON wrapping entity should be added to this.
 *
 * @param <T>
 */
public interface ModelWrappable<T> {
  T wrapped();

  boolean getWrapped();

  void setWrapped(boolean newWrapped);
}
