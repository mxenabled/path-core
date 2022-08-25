package com.mx.accessors;

import java.lang.reflect.Method;

import lombok.Builder;
import lombok.Data;

/**
 * Contains data for a method in an accessor that gets a child accessor.
 * todo: Move back to gateway after model extraction
 */
@Data
@Builder
public class AccessorMethodDefinition {
  /**
   * The accessor implementation class
   */
  private Class<? extends Accessor> accessorClass;

  /**
   * API annotation on accessor method
   */
  private API annotation;

  /**
   * Definition of accessor method from accessor base class
   */
  private AccessorMethodDefinition base;

  /**
   * An instance of the accessor. We currently need an instance of the accessor to describe it.
   */
  private Accessor instance;

  /**
   * The accessor method
   */
  private Method method;
}
