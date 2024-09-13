package com.mx.path.core.common.accessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a method should be invoked
 * after the accessor is initialized. Methods annotated
 * with @AfterAccessorInitialize are called automatically by the
 * accessor configurator to perform any necessary post-initialization actions.
 *
 * <p>Example Usage:</p>
 *
 * public class MyAccessor extends Accessor {
 *
 *     // This method will be invoked after the MyAccessor is initialized.
 *     #@AfterAccessorInitialize
 *     static void afterInitialize() {
 *         // Perform post-initialization tasks here
 *         System.out.println("MyAccessor has been initialized!");
 *     }
 * }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterAccessorInitialize {
}
