package com.mx.path.api.connect.messaging.remote.models;

public enum IDPState {
  AssignGroupID("AssignGroupID"),
  DeletePhoneNumber("DeletePhoneNumber"),
  DeleteUser("DeleteUser"),
  EmailVerify("EmailVerify"),
  GetUserID("GetUserID"),
  InitiateEmailOTP("InitiateEmailOTP"),
  InitiateExistingPhoneOTP("InitiateExistingPhoneOTP"),
  InitiatePhoneOTP("InitiatePhoneOTP"),
  LoginVerify("LoginVerify"),
  ResetPassword("ResetPassword"),
  UserCreate("UserCreate"),
  UpdateAndInitiatePhoneOTP("UpdateAndInitiatePhoneOTP"),
  UpdateClientId("UpdateClientId"),
  UpdateEmail("UpdateEmail"),
  UpdateMDXUserID("UpdateMDXUserID"),
  UpdatePassword("UpdatePassword"),
  UpdateProfile("UpdateProfile"),
  ValidateEmailOTP("ValidateEmailOTP"),
  ValidateExistingPhoneOTP("ValidateExistingPhoneOTP"),
  ValidatePhoneOTP("ValidatePhoneOTP");

  private final String stateName;

  IDPState(String stateName) {
    this.stateName = stateName;
  }

  public static IDPState getIDPStateName(String stateName) {
    return IDPState.valueOf(stateName);
  }
}
