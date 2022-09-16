package com.mx.common.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a model for internal use only. Will not be rendered in MDX responses (MDXv5 XML and MDXv6 JSON)
 */
@Target({ ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal {
}
