package com.mx.models.ondemand.mixins;

/**
 * Mixins allows us to modify model serialization without modifying the model directly with annotations.
 *
 * For more information on Jackson Mix-ins see:
 * https://github.com/FasterXML/jackson-docs/wiki/JacksonMixInAnnotations
 */
public class MixinDefinition {

  private final Class<?> mixin;
  private final Class<?> target;

  /**
   * @param target class to apply mixin during serialization
   * @param mixin definition
   */
  public MixinDefinition(Class<?> target, Class<?> mixin) {
    this.mixin = mixin;
    this.target = target;
  }

  public final Class<?> getMixin() {
    return mixin;
  }

  public final Class<?> getTarget() {
    return target;
  }
}
