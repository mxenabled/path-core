package com.mx.path.gateway.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to bind current ClientID to any configuration POJO
 *
 * <p><b>Configuration POJO</b>
 *
 * <pre>{@code
 *   @Data
 *   public class ConfigurationObj {
 *     @ClientID
 *     private String clientID;
 *   }
 * }</pre>
 *
 * <p><b>Accessor and AccessorConnectionSettings</b>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ClientID {
}
