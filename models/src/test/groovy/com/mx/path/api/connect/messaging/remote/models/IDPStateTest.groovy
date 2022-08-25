package com.mx.path.api.connect.messaging.remote.models

import spock.lang.Specification

class IDPStateTest extends Specification{

  def "test IDP states names"(){
    when:
    def response = IDPState.getIDPStateName("AssignGroupID")

    then:
    response == IDPState.AssignGroupID
    IDPState.DeletePhoneNumber == IDPState.getIDPStateName("DeletePhoneNumber")
    IDPState.DeleteUser == IDPState.getIDPStateName("DeleteUser")
    IDPState.EmailVerify == IDPState.getIDPStateName("EmailVerify")
    IDPState.GetUserID == IDPState.getIDPStateName("GetUserID")
    IDPState.InitiateEmailOTP == IDPState.getIDPStateName("InitiateEmailOTP")
    IDPState.InitiateExistingPhoneOTP == IDPState.getIDPStateName("InitiateExistingPhoneOTP")
    IDPState.InitiatePhoneOTP == IDPState.getIDPStateName("InitiatePhoneOTP")
    IDPState.LoginVerify == IDPState.getIDPStateName("LoginVerify")
    IDPState.ResetPassword == IDPState.getIDPStateName("ResetPassword")
    IDPState.UserCreate == IDPState.getIDPStateName("UserCreate")
    IDPState.UpdateAndInitiatePhoneOTP == IDPState.getIDPStateName("UpdateAndInitiatePhoneOTP")
    IDPState.UpdateClientId == IDPState.getIDPStateName("UpdateClientId")
    IDPState.UpdateEmail == IDPState.getIDPStateName("UpdateEmail")
    IDPState.UpdateMDXUserID == IDPState.getIDPStateName("UpdateMDXUserID")
    IDPState.UpdatePassword == IDPState.getIDPStateName("UpdatePassword")
    IDPState.UpdateProfile == IDPState.getIDPStateName("UpdateProfile")
    IDPState.ValidateEmailOTP == IDPState.getIDPStateName("ValidateEmailOTP")
    IDPState.ValidateExistingPhoneOTP == IDPState.getIDPStateName("ValidateExistingPhoneOTP")
    IDPState.ValidatePhoneOTP == IDPState.getIDPStateName("ValidatePhoneOTP")
  }
}
