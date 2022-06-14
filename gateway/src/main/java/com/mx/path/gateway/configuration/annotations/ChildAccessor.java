package com.mx.path.gateway.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mx.accessors.Accessor;

/**
 * Attach to Accessor class to indicate child accessor types.
 *
 * The {@link GatewayConfigurator} will auto-construct these accessors when building the
 * gateway stack.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ChildAccessors.class)
@Target(ElementType.TYPE)
public @interface ChildAccessor {
  Class<? extends Accessor> value();
}
