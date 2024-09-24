package com.mx.path.gateway.accessor;

import java.lang.reflect.Method;

import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.accessor.API;

/**
 * Contains data for a method in an accessor that gets a child accessor.
 *
 * todo: Move back to gateway after model extraction
 */
@Data
@Builder
public class AccessorMethodDefinition {
  /**
   * The accessor implementation class.
   *
   * -- GETTER --
   * Return accessor class.
   *
   * @return accessor class
   *
   * -- SETTER --
   * Set accessor class.
   *
   * @param accessorClass accessor class
   */
  private Class<? extends Accessor> accessorClass;

  /**
   * API annotation on accessor method.
   *
   * -- GETTER --
   * Return annotation API.
   *
   * @return annotation API
   *
   * -- SETTER --
   * Set annotation API.
   *
   * @param annotation annotation API
   */
  private API annotation;

  /**
   * Definition of accessor method from accessor base class.
   *
   * -- GETTER --
   * Return accessor definition method.
   *
   * @return accessor definition method
   *
   * -- SETTER --
   * Set accessor definition method.
   *
   * @param base accessor definition method
   */
  private AccessorMethodDefinition base;

  /**
   * An instance of the accessor. We currently need an instance of the accessor to describe it.
   *
   * -- GETTER --
   * Return accessor instance.
   *
   * @return accessor instance
   *
   * -- SETTER --
   * Set accessor instance.
   *
   * @param instance accessor instance
   */
  private Accessor instance;

  /**
   * The accessor method.
   *
   * -- GETTER --
   * Return accessor method.
   *
   * @return accessor method
   *
   * -- SETTER --
   * Set accessor method.
   *
   * @param method accessor method
   */
  private Method method;
}
