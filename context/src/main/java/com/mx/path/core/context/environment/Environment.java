package com.mx.path.core.context.environment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.mx.path.core.common.configuration.ConfigurationException;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import io.github.cdimascio.dotenv.DotenvEntry;

/**
 * Use this instead of {@link System#getenv(String)}
 *
 * System environment variables take precedence over variables set in the .env file. The .env file can be located in
 * the resources directory, or in the project root directory.
 */
public class Environment {

  private static Dotenv dotenv;
  private static Path dotEnvLocation = null;
  private static Map<String, String> dotEnv = null;
  private static Map<String, String> all = null;

  static Dotenv dotenv() {
    if (dotenv == null) {
      DotenvBuilder builder = Dotenv.configure().ignoreIfMissing();
      if (dotEnvLocation != null) {
        builder.filename(dotEnvLocation.toString());
      }

      dotenv = builder.load();
    }
    return dotenv;
  }

  static void setDotenv(Dotenv dotenv) {
    Environment.dotenv = dotenv;
  }

  static void reset() {
    dotEnvLocation = null;
    dotenv = null;
    all = null;
    dotEnv = null;
  }

  /**
   * Set the location of the .env file. Only needed if file is in a non-standard location.
   * @param location path to .env file
   */
  public static void setDotEnvLocation(String location) {
    Path path = Paths.get(location);

    if (!Files.exists(path)) {
      throw new ConfigurationException(".env location does not exist. " + location);
    }

    dotEnvLocation = path;
  }

  /**
   * Get environment variable value
   * @param key case-sensitive key
   * @return value
   */
  public static String get(String key) {
    return dotenv().get(key);
  }

  /**
   * Get environment variable value with default, if not set
   * @param key case-sensitive key
   * @param defaultValue default
   * @return value
   */
  public static String get(String key, String defaultValue) {
    return dotenv().get(key, defaultValue);
  }

  /**
   * @return Map of all environment variables (including those in ,env file)
   */
  public static Map<String, String> all() {
    if (all == null) {
      all = Collections.unmodifiableMap(dotenv()
          .entries()
          .stream()
          .collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue)));
    }

    return all;
  }

  /**
   * @return Map of all environment variables found in the .env file
   */
  public static Map<String, String> dotEnv() {
    if (dotEnv == null) {
      dotEnv = Collections.unmodifiableMap(dotenv()
          .entries(Dotenv.Filter.DECLARED_IN_ENV_FILE)
          .stream()
          .collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue)));
    }

    return dotEnv;
  }

  /**
   * @return Map of all system environment variables (not including those in ,env file)
   */
  public static Map<String, String> system() {
    return System.getenv();
  }
}
