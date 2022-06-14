package com.mx.models.profile;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class Profile extends MdxBase<Profile> {
  @SerializedName("birth_date")
  private LocalDate birthDate;
  @SerializedName("first_name")
  private String firstName;
  private Gender gender;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("middle_name")
  private String middleName;
  private String ssn;

  public Profile() {
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  // enum definition

  public enum Gender {
    @SerializedName("MALE")
    MALE("Male"), @SerializedName("FEMALE")
    FEMALE("Female"), @SerializedName("UNDECLARED")
    UNDECLARED("Gender Undeclared");

    private String description;

    Gender(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }

  }
}
