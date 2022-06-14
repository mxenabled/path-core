package com.mx.common.remote;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @RemoteOperation() annotation can be added to an accessor method to override the generated method name in the
 * associated RemoteGateway, and the generated operation name used in the RemoteAccessor. Any accessor method can make
 * use of this annotation; however, overloaded accessor operations require it.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteOperation {
  String value();
}
