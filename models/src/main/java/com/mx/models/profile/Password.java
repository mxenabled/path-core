package com.mx.models.profile;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class Password extends MdxBase<Password> {
  @SerializedName("current_password")
  private String currentPassword;
  @SerializedName("new_password")
  private String newPassword;

  public Password() {
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
