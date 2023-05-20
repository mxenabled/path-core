package com.mx.path.core.common.collection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that allows a class to declare it's preferred ordering in a collection.
 *
 * <p>Intended to be used with {@link OrderComparator}. See {@link Ordered} for constants
 * that can be used for {@link #value()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Order {
  int value();
}
