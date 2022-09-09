package com.mx.common.gateway;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GatewayBaseClass {

  Class annotation();

  String className() default "Gateway";

  String namespace() default "";

  // todo: Figure out how to constrain this to extends Accessor
  Class<?> target();

}
