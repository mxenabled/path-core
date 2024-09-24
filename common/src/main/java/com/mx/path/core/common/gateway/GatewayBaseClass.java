package com.mx.path.core.common.gateway;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Gateway interface.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GatewayBaseClass {

  /**
   * @return return class name
   */
  String className() default "Gateway";

  /**
   * @return namespace
   */
  String namespace() default "";

  /**
   * @return target class
   */
  // todo: Figure out how to constrain this to extends Accessor
  Class<?> target();

}
