package com.mx.path.api.reporting;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public final class ClassGenerationException extends RuntimeException {
  private String className;
  private String humanReadableError;
  private String fixInstructions;

  public ClassGenerationException() {

  }

  public ClassGenerationException(String className, String humanReadableError, String fixInstructions) {
    this.className = className;
    this.humanReadableError = humanReadableError;
    this.fixInstructions = fixInstructions;
  }

  public ClassGenerationException withClassName(String newClassName) {
    this.className = newClassName;
    return this;
  }

  public ClassGenerationException withHumanReadableError(String newHumanReadableError) {
    this.humanReadableError = newHumanReadableError;
    return this;
  }

  public ClassGenerationException withFixInstructions(String newFixInstructions) {
    this.fixInstructions = newFixInstructions;
    return this;
  }
}
