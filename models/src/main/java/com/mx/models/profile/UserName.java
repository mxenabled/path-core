package com.mx.models.profile;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public class UserName extends MdxBase<UserName> {
  @SerializedName("new_username")
  private String newUserName;

  public final String getNewUserName() {
    return newUserName;
  }

  public final void setNewUserName(String newUserName) {
    this.newUserName = newUserName;
  }
}
