package com.mx.path.model.context;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

public class SessionAccountOwner {

  // Fields

  private String firstName;
  private String fullName;
  private String lastName;

  // Getter/Setters

  public final String getFirstName() {
    return firstName;
  }

  public final void setFirstName(String firstName) {
    this.firstName = StringUtils.defaultIfBlank(firstName, null);
  }

  public final String getFullName() {
    return fullName;
  }

  public final void setFullName(String fullName) {
    this.fullName = StringUtils.defaultIfBlank(fullName, null);
  }

  public final String getLastName() {
    return lastName;
  }

  public final void setLastName(String lastName) {
    this.lastName = StringUtils.defaultIfBlank(lastName, null);
  }

  // Constructor

  public SessionAccountOwner() {
    super();
  }

  public SessionAccountOwner(SessionAccountOwner toCopy) {
    this.setFirstName(toCopy.firstName);
    this.setLastName(toCopy.lastName);
    this.setFullName(toCopy.fullName);
  }

  // Public

  /**
   * Attempts to pull apart a comma-separated full name into lastname, firstname.
   *
   * @param commaSeparatedFullName
   */
  public final void commaSeparatedFullName(String commaSeparatedFullName) {
    setFullName(commaSeparatedFullName);

    if (StringUtils.isBlank(commaSeparatedFullName)) {
      setFirstName(null);
      setLastName(null);

      return;
    }

    String[] parts = commaSeparatedFullName.split(",");

    if (parts.length >= 1 && Objects.nonNull(parts[0])) {
      setLastName(parts[0].trim());
    }

    if (parts.length >= 2 && Objects.nonNull(parts[1])) {
      setFirstName(parts[1].trim());
    }
  }
}
