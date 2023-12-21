package org.apache.commons.text;

import java.util.HashMap;
import java.util.Map;

import com.mx.path.core.context.environment.Environment;

import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

/**
 * The EnvironmentStringSubstitutor class is a string substitutor that replaces variables in a string with their
 * corresponding values from the environment. It uses the ${@link Environment} class instead of
 * {@link System#getenv()} to retrieve the values, allowing it to pull in values set by a .env file in addition to
 * those set in system environment variables.
 *
 * Example Usage
 * String input = "Hello, ${env:NAME}!";
 * String output = EnvironmentStringSubstitutor.replace(input);
 * System.out.println(output);
 * // Output: Hello, John!
 *
 * // Assuming the environment variable "NAME" is set to "John"
 *
 * The main functionality of the EnvironmentStringSubstitutor class is to replace variables in a string with their
 * corresponding values from the environment. It supports variables in the format ${env:VAR_NAME} where VAR_NAME is the
 * name of the environment variable.
 */
public class EnvironmentStringSubstitutor {
  private static final StringSubstitutor SUBSTITUTOR;

  static {
    Map<String, StringLookup> stringLookupMap = new HashMap<>();

    // Add custom lookups
    stringLookupMap.put(StringLookupFactory.KEY_ENV, new EnvironmentStringLookup());

    // Add default lookups
    stringLookupMap.put(StringLookupFactory.KEY_BASE64_DECODER, StringLookupFactory.INSTANCE.base64DecoderStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_BASE64_ENCODER, StringLookupFactory.INSTANCE.base64EncoderStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_CONST, StringLookupFactory.INSTANCE.constantStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_DATE, StringLookupFactory.INSTANCE.dateStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_FILE, StringLookupFactory.INSTANCE.fileStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_JAVA, StringLookupFactory.INSTANCE.javaPlatformStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_LOCALHOST, StringLookupFactory.INSTANCE.localHostStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_PROPERTIES, StringLookupFactory.INSTANCE.propertiesStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_RESOURCE_BUNDLE, StringLookupFactory.INSTANCE.resourceBundleStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_SYS, StringLookupFactory.INSTANCE.systemPropertyStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_URL_DECODER, StringLookupFactory.INSTANCE.urlDecoderStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_URL_ENCODER, StringLookupFactory.INSTANCE.urlEncoderStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_XML, StringLookupFactory.INSTANCE.xmlStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_XML_DECODER, StringLookupFactory.INSTANCE.xmlDecoderStringLookup());
    stringLookupMap.put(StringLookupFactory.KEY_XML_ENCODER, StringLookupFactory.INSTANCE.xmlEncoderStringLookup());

    SUBSTITUTOR = new StringSubstitutor(
        StringLookupFactory.INSTANCE.interpolatorStringLookup(
            stringLookupMap,
            null,
            false));
  }

  static class EnvironmentStringLookup implements StringLookup {
    /**
     * lookup(String key): Looks up value from {@link Environment}.
     *
     * @param key the key to look up, may be null.
     * @return
     */
    @Override
    public String lookup(String key) {
      return Environment.get(key);
    }
  }

  /**
   * Does interpolator replacements using a custom StringSubstitutor that replaces the default System.getenv() lookup
   * with Environment.get() for "env:" variables.
   *
   * @param val input string
   * @return replaced string
   */
  public static String replace(String val) {
    if (val == null) {
      return null;
    }

    return SUBSTITUTOR.replace(val);
  }
}
