package com.mx.path.core.context.environment;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;

/**
 * String substitutor for environment variables. Uses {@link Environment} instead of {@link System#getenv(String)}.
 * This allows it to pull in values set by a .env file in addition to those set in system environment variables.
 */
public class EnvironmentStringSubstitutor {
  static class EnvironmentStringLookup implements StringLookup {
    @Override
    public String lookup(String key) {
      if (key.startsWith("env:")) {
        key = key.replaceFirst("^env:", "");
        String value = Environment.dotEnv().get(key);
        if (value != null) {
          return value;
        }
      }

      // No match or not an env variable. Returning null will leave the value to be processed by another substitutor.
      return null;
    }
  }

  /**
   * Replace with environment variables
   *
   * @param val input string
   * @return replaced string
   */
  public static String replace(String val) {
    String newVal = new StringSubstitutor(new EnvironmentStringLookup()).replace(val);
    return StringSubstitutor.createInterpolator().replace(newVal);
  }
}
