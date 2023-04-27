package com.mx.path.core.common.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A ParameterizedTypeImpl represents a parameterized type such as ModelList&lt;Account&gt;.
 * <p>
 * In this example 'ModelList' is referred to as the 'raw type' and 'Account' is referred to as the 'actual type'.
 * This is useful to understand when using the methods 'getActualTypeArguments' and 'getRawType'.
 * <p>
 * Two constructors are provided by the ParameterizedTypeImpl implementation.
 * In one only an actual type is provided, and the raw type is a ModelList.
 * In the other, both the raw type and actual type are specified, so any types can be used, such as MdxList&lt;Account&gt;
 */
public final class ParameterizedTypeImpl<T> implements ParameterizedType {
  private final Class<?> actualType;
  private final Class<?> rawType;

  /**
   * Constructor that specifies the actual type. e.g. rawType&lt;actualType&gt;
   * rawType will be ModelList
   * @param actualType - The type inside the brackets
   */
  public ParameterizedTypeImpl(Class<T> actualType) {
    this.actualType = actualType;
    this.rawType = ModelList.class;
  }

  /**
   * Constructor that specifies the raw type, and the actual type. e.g. rawType&lt;actualType&gt;
   * @param rawType - The type outside the brackets
   * @param actualType - The type inside the brackets
   */
  public ParameterizedTypeImpl(Class<?> rawType, Class<T> actualType) {
    this.rawType = rawType;
    this.actualType = actualType;
  }

  /**
   * Gets the types inside the brackets of the ParameterizedType
   */
  @Override
  public Type[] getActualTypeArguments() {
    return new Type[] { actualType };
  }

  /**
   * Gets the type outside the brackets of the ParameterizedType
   */
  @Override
  public Type getRawType() {
    return rawType;
  }

  /**
   * Returns null for this implementation of ParameterizedType
   */
  @Override
  public Type getOwnerType() {
    return null;
  }
}
