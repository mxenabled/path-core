package com.mx.path.utilities.OAuth

import spock.lang.Specification

class OAuthUtilityTest extends Specification  {
  def "test generate Basic AuthorizationToken "(){
    /***   everything valid scenario ***/
    when:
    String token = OAuthUtility.generateBasicAuthorizationToken("token@mx.com","4<Qqt02v~o(")

    then:
    token == "Basic dG9rZW5AbXguY29tOjQ8UXF0MDJ2fm8o"

    /***   empty case scenario ***/
    when:
    token = OAuthUtility.generateBasicAuthorizationToken("","")

    then:
    token == ""

    /***   null case scenario ***/
    when:
    token = OAuthUtility.generateBasicAuthorizationToken(null,null)

    then:
    token == ""
  }
}
