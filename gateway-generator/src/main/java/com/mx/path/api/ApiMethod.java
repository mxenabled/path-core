package com.mx.path.api;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.mx.path.core.common.model.ModelBase;
import com.mx.path.core.common.model.ModelList;
import com.mx.path.gateway.accessor.AccessorResponse;

/**
 * Utility method class for {@link Method}.
 */
@SuppressWarnings("checkstyle:IllegalImport")
public class ApiMethod {

  /**
   * {@link Method} object wrapped by this class.
   */
  private final Method method;

  /**
   * Build new {@link ApiMethod} instance with given {@link Method}.
   *
   * @param method {@link Method} to wrap
   */
  public ApiMethod(Method method) {
    this.method = method;
  }

  /**
   * Return generic return type of method.
   *
   * @return generic return type
   */
  public final Type getGenericReturnType() {
    return getMethod().getGenericReturnType();
  }

  /**
   * Return {@link Method} object wrapped by this class.
   *
   * @return wrapped {@link Method}
   */
  public final Method getMethod() {
    return method;
  }

  /**
   * Return method name.
   *
   * @return method name
   */
  public final String getName() {
    return method.getName();
  }

  /**
   * Return list of {@link Parameter} from method.
   *
   * @return list of method parameters
   */
  public final List<Parameter> getParameters() {
    return Arrays.asList(method.getParameters());
  }

  /**
   * Checks if the method is valid based on:
   * - Return type must be {@link AccessorResponse}.
   * - Model type must be assignable from {@link ModelBase}.
   *
   * @return true if the method is valid, otherwise false
   */
  public final boolean isValid() {
    if (method.getReturnType() != AccessorResponse.class) {
      return false;
    }

    try {
      if (!ModelBase.class.isAssignableFrom(typeAsClass(getModel()))) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }

    return true;

  }

  /**
   * Retrieves type of method model. If the return type is a list, it unwraps the list
   * to get the type of its elements.
   *
   * @return model type
   */
  public final Type getModel() {
    Type returnType = unwrappedReturnType();
    if (isList(returnType)) {
      return unwrapType(returnType);
    }

    return returnType;
  }

  /**
   * Checks if the method's return type is a list operation.
   *
   * @return true if the return type is a list, otherwise false
   */
  public final boolean isListOp() {
    return isList(unwrappedReturnType());
  }

  @SuppressWarnings("PMD.EmptyCatchBlock")
  private Class<?> typeAsClass(Type t) {
    try {
      ParameterizedType parameterizedType = (ParameterizedType) t;
      return (Class<?>) parameterizedType.getRawType();
    } catch (ClassCastException e) {
      // ignore
    }

    return (Class<?>) t;
  }

  private Type unwrappedReturnType() {
    Type[] genericTypeParameters = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
    return genericTypeParameters[0];
  }

  private Type unwrapType(Type t) {
    ParameterizedType parameterizedType = (ParameterizedType) t;
    return parameterizedType.getActualTypeArguments()[0];
  }

  private boolean isList(Type t) {
    return ModelList.class.isAssignableFrom(typeAsClass(t));
  }
}
