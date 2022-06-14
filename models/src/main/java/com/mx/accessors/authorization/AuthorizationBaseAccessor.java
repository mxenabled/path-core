package com.mx.accessors.authorization;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.authorization.Authorization;
import com.mx.models.authorization.HtmlPage;

/**
 * Accessor base for authorization operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/authorization/#mdx-authorization")
public abstract class AuthorizationBaseAccessor extends Accessor {

  public AuthorizationBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create authorization
   * @param authorization
   * @return
   */
  @GatewayAPI
  @API(description = "Create authorization")
  public AccessorResponse<Authorization> create(Authorization authorization) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * An HTML responder used to handle non-standard webview requirements.
   * @param token needed to continue SSO
   * @return HTML
   */
  @GatewayAPI
  @API(description = "An HTML responder used to handle non-standard webview requirements.")
  public AccessorResponse<HtmlPage> callback(String token) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
