package com.mx.path.api.reporting;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Exception class for handling errors related to class generation.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class ClassGenerationException extends RuntimeException {

  /**
   * -- GETTER --
   * Return class name.
   *
   * @return class name
   *
   * -- SETTER --
   * Set class name.
   *
   * @param className class name to set
   */
  private String className;

  /**
   * -- GETTER --
   * Return human readable error.
   *
   * @return human readable error
   *
   * -- SETTER --
   * Set human readable error.
   *
   * @param humanReadableError human readable error to set
   */
  private String humanReadableError;

  /**
   * -- GETTER --
   * Return fix instructions.
   *
   * @return fix instructions
   *
   * -- SETTER --
   * Set fix instructions.
   *
   * @param fixInstructions fix instructions to set
   */
  private String fixInstructions;

  /**
   * Default constructor.
   */
  public ClassGenerationException() {

  }

  /**
   * Build new {@link ClassGenerationException} with specified parameters.
   *
   * @param className name of the class where the error occurred
   * @param humanReadableError a human-readable description of the error
   * @param fixInstructions instructions on how to fix the issue
   */
  public ClassGenerationException(String className, String humanReadableError, String fixInstructions) {
    this.className = className;
    this.humanReadableError = humanReadableError;
    this.fixInstructions = fixInstructions;
  }

  /**
   * Set class name and return updated exception instance.
   *
   * @param newClassName class name to set
   * @return self
   */
  public ClassGenerationException withClassName(String newClassName) {
    this.className = newClassName;
    return this;
  }

  /**
   * Set human-readable error and return updated exception instance.
   *
   * @param newHumanReadableError human-readable error to set
   * @return self
   */
  public ClassGenerationException withHumanReadableError(String newHumanReadableError) {
    this.humanReadableError = newHumanReadableError;
    return this;
  }

  /**
   * Set fix instructions and return updated exception instance.
   *
   * @param newFixInstructions fix instructions to set
   * @return self
   */
  public ClassGenerationException withFixInstructions(String newFixInstructions) {
    this.fixInstructions = newFixInstructions;
    return this;
  }
}
