package com.mx.path.gateway.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field or constructor parameter as a connection for the purposes of auto-binding
 * of the instance and auto-binding the connection configuration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface Connection {

  /**
   * The connection name to be bound from gateway configuration.
   *
   * @return connection name
   */
  String value();

  /**
   * Indicates whether the connection is optionally required to be configured in the gateway configuration. Default is false.
   *
   * @return false if connection is not required to be configured on gateway, true otherwise
   */
  boolean optional() default false;
}
