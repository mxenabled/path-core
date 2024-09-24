package com.mx.path.connect.http.certificate;

import java.util.List;

/**
 * Exception to be thrown when there is an error on field settings validation.
 */
public class FieldSettingsValidationError extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Represents field validation error.
   */
  public static class Field {
    private String error;
    private String field;

    /**
     * Return error message associated with field validation failure.
     *
     * @return error message
     */
    public final String getError() {
      return this.error;
    }

    /**
     * Set the error message associated with field validation failure.
     *
     * @param error error message to set
     */
    public final void setError(String error) {
      this.error = error;
    }

    /**
     * return name of field that failed validation.
     *
     * @return field name
     */
    public final String getField() {
      return this.field;
    }

    /**
     * Sets name of field that failed validation.
     *
     * @param field field name to set
     */
    public final void setField(String field) {
      this.field = field;
    }

    /**
     * Build new {@link Field} object with specified field name and error message.
     *
     * @param field name of field that failed validation
     * @param error error message associated with failed field validation
     */
    public Field(String field, String error) {
      this.field = field;
      this.error = error;
    }
  }

  private transient List<Field> fields;

  /**
   * Return list of field validation errors.
   *
   * @return a list of {@link Field} objects representing the validation errors
   */
  public final List<Field> getFields() {
    return fields;
  }

  /**
   * Set list of field validation errors.
   *
   * @param fields list of {@link Field} objects to set
   */
  public final void setFields(List<Field> fields) {
    this.fields = fields;
  }

  /**
   * Build new {@link FieldSettingsValidationError} with specified list of field errors.
   *
   * @param fields list of {@link Field} objects representing the validation errors
   */
  public FieldSettingsValidationError(List<Field> fields) {
    this.setFields(fields);
  }

  /**
   * Build new {@link  FieldSettingsValidationError} with specified error message.
   *
   * @param message error message describing the validation failure
   */
  public FieldSettingsValidationError(String message) {
    super(message);
  }
}
