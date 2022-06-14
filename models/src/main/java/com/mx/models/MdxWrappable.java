package com.mx.models;

public interface MdxWrappable<T> {
  T wrapped();

  boolean getWrapped();

  void setWrapped(boolean newWrapped);
}
