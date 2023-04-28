package com.mx.path.connect.http.certificate;

import java.util.List;

public class FieldSettingsValidationError extends Exception {
  private static final long serialVersionUID = 1L;

  public static class Field {
    private String error;
    private String field;

    public final String getError() {
      return this.error;
    }

    public final void setError(String error) {
      this.error = error;
    }

    public final String getField() {
      return this.field;
    }

    public final void setField(String field) {
      this.field = field;
    }

    public Field(String field, String error) {
      this.field = field;
      this.error = error;
    }
  }

  private transient List<Field> fields;

  public final List<Field> getFields() {
    return fields;
  }

  public final void setFields(List<Field> fields) {
    this.fields = fields;
  }

  public FieldSettingsValidationError(List<Field> fields) {
    this.setFields(fields);
  }

  public FieldSettingsValidationError(String message) {
    super(message);
  }
}
