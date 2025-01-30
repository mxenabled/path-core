package com.mx.path.core.common.security;

public class LogValueRegex {

  public static String jsonString(String fieldName) {
    return String.format("\"%s\"\\s*:\\s*\"(.+?)\"", fieldName);
  }

  public static String jsonNumber(String fieldName) {
    return String.format("\"%s\"\\s*:\\s*([-+]?\\d+\\.?\\d*E?\\+?\\d*)", fieldName);
  }

  public static String jsonArray(String fieldName) {
    return String.format("\"%s\"\\s*:\\s*\\[([^]]+?)\\]", fieldName);
  }
}
