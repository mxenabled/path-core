package com.mx.path.core.common.lang;

public class Strings {

  public static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  public static boolean isNotBlank(String s) {
    return !isBlank(s);
  }

}
