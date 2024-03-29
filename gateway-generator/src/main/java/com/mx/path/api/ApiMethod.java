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

@SuppressWarnings("checkstyle:IllegalImport")
public class ApiMethod {
  private final Method method;

  public ApiMethod(Method method) {
    this.method = method;
  }

  public final Type getGenericReturnType() {
    return getMethod().getGenericReturnType();
  }

  public final Method getMethod() {
    return method;
  }

  public final String getName() {
    return method.getName();
  }

  public final List<Parameter> getParameters() {
    return Arrays.asList(method.getParameters());
  }

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

  public final Type getModel() {
    Type returnType = unwrappedReturnType();
    if (isList(returnType)) {
      return unwrapType(returnType);
    }

    return returnType;
  }

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
