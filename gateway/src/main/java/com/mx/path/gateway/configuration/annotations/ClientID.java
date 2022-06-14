package com.mx.path.gateway.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to bind current ClientID to any configuration POJO, AccessorConnection, or Accessor
 *
 * <p>
 * <b>Configuration POJO</b>
 *
 * <p>
 * <i>Note: This is the only place that the annotation is applicable to a field. Other uses must appear as a constructor param.</i>
 *
 * <p>
 * <pre>
 * {@code
 *   @Data
 *   public class ConfigurationObj {
 *     @ClientID
 *     private String clientID;
 *   }
 * }
 * </pre>
 *
 * <b>Accessor and AccessorConnection</b>
 *
 * <p>
 * <pre>
 *   {@code
 *   public class IdAccessor extends BaseIdAccessor {
 *     private String clientId;
 *
 *     public IdAccessor(AccessorConfiguration configuration, @ClientID String clientId) {
 *       this.clientId = clientId;
 *     }
 *   }
 *   }
 * </pre>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface ClientID {
}
