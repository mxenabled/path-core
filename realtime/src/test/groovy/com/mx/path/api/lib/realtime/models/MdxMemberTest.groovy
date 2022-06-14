package com.mx.path.api.lib.realtime.models

import spock.lang.Specification

class MdxMemberTest extends Specification {

  def "test for getter/setter"() {
    given:
    def member = new MdxMember()

    when:
    member.setId("id")
    member.setGuid("guid")
    member.setIsDisabled(true)
    member.setInstitutionId("institution_id")
    member.setMetadata("metadata")
    member.setName("name")
    member.setUserGuid("user_guid")
    member.setUserId("user_id")
    member.setLogin("login")
    member.setPassword("password")
    member.setUserKey("userKey")

    then:
    member.getId() == "id"
    member.getGuid() == "guid"
    member.getIsDisabled() == true
    member.getInstitutionId() == "institution_id"
    member.getMetadata() == "metadata"
    member.getName() == "name"
    member.getUserGuid() == "user_guid"
    member.getUserId() == "user_id"
    member.getLogin() == "login"
    member.getPassword() == "password"
    member.getUserKey() == "userKey"
  }
}
