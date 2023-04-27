package com.mx.path.core.common.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used by accessors to specify which upstream service they interact with. This is used to determine how
 * session read/writes should be scoped to prevent key collisions across services.
 *
 * Required by BaseAccessors, but can be added to individual accessors as well.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScope {
  /**
   * The name of the service the accessor interacts with.
   */
  String value();
}
