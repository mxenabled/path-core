package com.mx.common.models;

/**
 * Interface for wrappable MDX objects. Used to determine if a JSON wrapping entity should be added to this.
 *
 * todo: Need to decide what to do with this since it still references MDX.
 * @param <T>
 */
public interface MdxWrappable<T> {
  T wrapped();

  boolean getWrapped();

  void setWrapped(boolean newWrapped);
}
