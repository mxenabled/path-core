package com.mx.common.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Base for all MDX models
 *
 * todo: Need to decide what to do with this since it still references MDX.
 */
public abstract class MdxBase<T> implements MdxWrappable<T> {

  @Internal
  private final Map<String, String> objectMetadata = new LinkedHashMap<>();
  private String userId;
  // Indicates if this mdx object should be wrapped in Json object.
  private transient boolean wrapped = false;

  private List<Warning> warnings;

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

  public final String getUserId() {
    return this.userId;
  }

  public final void setUserId(String newUserId) {
    this.userId = newUserId;
  }

  public final List<Warning> getWarnings() {
    return this.warnings;
  }

  public final void appendWarning(Warning warning) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<Warning>();
    }
    this.warnings.add(warning);
  }
}
