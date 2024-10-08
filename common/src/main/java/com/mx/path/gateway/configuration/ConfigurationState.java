package com.mx.path.gateway.configuration;

import java.util.Stack;
import java.util.function.Supplier;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.mx.path.core.common.lang.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to keep track of the current configuration tree state.
 *
 * <p>Can be referenced to give configuration state when an error is encountered.
 */
public final class ConfigurationState {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationState.class);

  private static ConfigurationState current = new ConfigurationState();

  /**
   * Return current state instance.
   * @return current
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static ConfigurationState getCurrent() {
    return current;
  }

  /**
   * Used for testing.
   */
  static void resetCurrent() {
    current = new ConfigurationState();
  }

  private String field;

  private final Stack<String> state = new Stack<>();

  /**
   * Default constructor.
   */
  private ConfigurationState() {
  }

  /**
   * Return current state as string.
   *
   * @return current state
   */
  public String currentState() {
    return String.join(".", this.state);
  }

  /**
   * Field to configure.
   *
   * @param currentField field
   */
  public void field(String currentField) {
    this.field = currentField;
    if (Strings.isNotBlank(this.field)) {
      LOGGER.debug("Configuring: {}#{}", this.currentState(), this.field);
    }
  }

  /**
   * Return field.
   *
   * @return field
   */
  public String field() {
    return this.field;
  }

  /**
   * Remove top object from state stack.
   *
   * @return object removed
   */
  public String popLevel() {
    return this.state.pop();
  }

  /**
   * Push new object to stack.
   *
   * @param nextLevel object to push
   */
  public void pushLevel(String nextLevel) {
    this.state.push(nextLevel);
    LOGGER.debug("Configuration: {}", this.currentState());
  }

  /**
   * Run with field.
   *
   * @param currentField field
   * @param runnable runnable
   */
  public void withField(String currentField, Runnable runnable) {
    field(currentField);
    try {
      try {
        runnable.run();
      } catch (ConfigurationError e) {
        throw e;
      } catch (Exception e) {
        throw new ConfigurationError("A configuration error occurred", this, e);
      }
    } finally {
      field(null);
    }
  }

  /**
   * Get from supplier with field.
   *
   * @param currentField field to set
   * @param supplier supplier
   * @return return supplier result, null if exception thrown
   * @param <T> return type
   */
  public <T> T withField(String currentField, Supplier<T> supplier) {
    field(currentField);
    try {
      try {
        return supplier.get();
      } catch (ConfigurationError e) {
        throw e;
      } catch (Exception e) {
        throw new ConfigurationError("A configuration error occurred", this, e);
      }
    } finally {
      field(null);
    }
  }

  /**
   * Run and push level.
   *
   * @param nextLevel level to push
   * @param runnable runnable
   */
  public void withLevel(String nextLevel, Runnable runnable) {
    pushLevel(nextLevel);
    try {
      try {
        runnable.run();
      } catch (ConfigurationError e) {
        throw e;
      } catch (Exception e) {
        throw new ConfigurationError("A configuration error occurred", this, e);
      }
    } finally {
      popLevel();
    }
  }

  /**
   * Get from supplier and push new level.
   *
   * @param nextLevel level to push
   * @param supplier supplier
   * @return supplier result, null if exception thrown
   * @param <T> return type
   */
  public <T> T withLevel(String nextLevel, Supplier<T> supplier) {
    pushLevel(nextLevel);
    try {
      try {
        return supplier.get();
      } catch (ConfigurationError e) {
        throw e;
      } catch (Exception e) {
        throw new ConfigurationError("A configuration error occurred", this, e);
      }
    } finally {
      popLevel();
    }
  }
}
