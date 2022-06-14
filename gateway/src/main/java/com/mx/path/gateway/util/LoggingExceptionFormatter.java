package com.mx.path.gateway.util;

import java.util.Arrays;

public class LoggingExceptionFormatter {
  public static String formatLoggingException(Exception exception) {
    StringBuilder exceptionString = new StringBuilder();
    exceptionString.append(exception.getClass().toString().replace("class ", "")).append(": ").append(exception.getMessage());
    if (exception.getCause() != null) {
      exceptionString.append("\n\tCaused by: ").append(exception.getCause());
    }
    return exceptionString.toString();
  }

  public static String formatLoggingExceptionWithStacktrace(Exception exception) {
    StringBuilder exceptionString = new StringBuilder();
    exceptionString.append(exception.getClass().toString().replace("class ", "")).append(": ").append(exception.getMessage());

    String stackTrace = Arrays.toString(exception.getStackTrace());
    stackTrace = stackTrace.substring(1, stackTrace.indexOf(",", stackTrace.indexOf(",", stackTrace.indexOf(",", stackTrace.indexOf("com.mx")) + 1) + 1));
    exceptionString.append("\n\t").append(stackTrace.replaceAll(",", "\n\t\t"));

    if (exception.getCause() != null) {
      exceptionString.append("\n\tCaused by: ").append(exception.getCause());
      stackTrace = Arrays.toString(exception.getCause().getStackTrace());
      stackTrace = stackTrace.substring(1, stackTrace.indexOf(",", stackTrace.indexOf(",", stackTrace.indexOf(",", stackTrace.indexOf("com.mx")) + 1) + 1));
      exceptionString.append("\n\t\t").append(stackTrace.replaceAll(",", "\n\t\t\t"));
    }
    return exceptionString.toString();
  }
}
