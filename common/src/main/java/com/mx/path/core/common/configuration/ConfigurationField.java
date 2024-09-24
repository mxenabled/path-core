package com.mx.path.core.common.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as bindable to a field in a @Configuration POJO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationField {

  /**
   * The field name to be bound from gateway configuration. Default is the name of the POJO field.
   *
   * @return field value
   */
  String value() default "";

  /**
   * Description of the field. Meant for configuration documentation purposes. Default is blank.
   *
   * @return field description
   */
  String description() default "";

  /**
   * Indicates if a value is required to be provided in the configuration file. Default is false.
   *
   * @return true if field is mandatory
   */
  boolean required() default false;

  /**
   * Indicates the type the value should be coerced to. Default is the type of the POJO field.
   *
   * @return field type
   */
  Class<?> elementType() default Void.class; // inferred from field type when void

  /**
   * Used by scaffolding to fill in default value. Can be a valid value intended to be adjusted, or an example.
   *
   * @return placeholder
   */
  String placeholder() default "";

  /**
   * Set on String values to indicate that the value should not be logged.
   *
   * @return true if field is secret
   */
  boolean secret() default false;
}
