package com.mx.path.api.connect.messaging.remote;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.mx.models.MdxList;

public final class MdxListOfJson<T> implements ParameterizedType {
  private Class<?> wrapped;

  public MdxListOfJson(Class<T> wrapper) {
    this.wrapped = wrapper;
  }

  @Override
  public Type[] getActualTypeArguments() {
    return new Type[] { wrapped };
  }

  @Override
  public Type getRawType() {
    return MdxList.class;
  }

  @Override
  public Type getOwnerType() {
    return null;
  }
}
