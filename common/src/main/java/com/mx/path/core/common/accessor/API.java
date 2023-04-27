package com.mx.path.core.common.accessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to call out and add documentation to MDX APIs.
 * todo: Move back to gateway after model extraction
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface API {
  String description() default "";

  String notes() default "";

  String specificationUrl() default "";
}
