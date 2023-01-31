package com.mx.common.gateway;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by gateway annotation processor to generate gateways. Place on the gateway base class to be used for all
 * generated gateways
 *
 * <p>
 * todo: V2 - Move to gateway project
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GatewayBaseClass {

  String className() default "Gateway";

  String namespace() default "";

  // todo: Figure out how to constrain this to extends Accessor
  Class<?> target();

  /**
   * Class name for an implementation of GatewayInitializer (in gateway project)
   * @return
   */
  String initializer() default "";
}
