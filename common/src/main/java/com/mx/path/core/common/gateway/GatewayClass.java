package com.mx.path.core.common.gateway;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by annotation processor to build gateway for accessors.
 *
 * <p>Mark accessor methods and sub-gateway fields with this annotation.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GatewayClass {
}
