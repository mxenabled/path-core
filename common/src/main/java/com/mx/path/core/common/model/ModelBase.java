package com.mx.path.core.common.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base for all MDX models
 *
 * todo: Need to decide what to do with this since it still references MDX.
 */
public abstract class ModelBase<T> implements ModelWrappable<T> {

  @Internal
  private final Map<String, String> objectMetadata = new LinkedHashMap<>();
  // Indicates if this object should be wrapped in Json object.
  private transient boolean wrapped = false;

  /**
   * Mark resource as wrapped.
   * Override to modify wrapping behavior
   * @return wrapped T
   */
  @SuppressWarnings("unchecked")
  @Override
  public T wrapped() {
    this.setWrapped(true);
    return (T) this;
  }

  /**
   * Stores object metadata for internal use
   * @return map of key/value pairs
   */
  public final Map<String, String> getObjectMetadata() {
    return objectMetadata;
  }

  @Override
  public final boolean getWrapped() {
    return wrapped;
  }

  @Override
  public final void setWrapped(boolean newWrapped) {
    wrapped = newWrapped;
  }
}
