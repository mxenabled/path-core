package com.mx.accessors.origination;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.challenges.Challenge;
import com.mx.models.origination.Origination;

/**
 * Accessor for origination operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/origination/#mdx-orignation">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/origination/#mdx-origination")
public abstract class OriginationBaseAccessor extends Accessor {

  public OriginationBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Start Origination
   * @return
   */
  @GatewayAPI
  @API(description = "start Origination process will return challenge as per client config")
  public AccessorResponse<Origination> start() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * answer a challenge
   * @param id
   * @param challenge
   * @return
   */
  @GatewayAPI
  @API(description = "answer an Origination challenge")
  public AccessorResponse<Origination> answerChallenge(String id, String challengeId, Challenge challenge) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Authenticated user origination start
   * @return
   */
  @GatewayAPI
  @API(description = "start Origination process will return challenge as per client config for an authenticated user")
  public AccessorResponse<Origination> authenticatedUserStart(String userId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * answer a challenge for an authenticated user
   * @param id
   * @param challenge
   * @return
   */
  @GatewayAPI
  @API(description = "answer an Origination challenge for an authenticated user")
  public AccessorResponse<Origination> authenticatedUserAnswerChallenge(String userId, String id, String challengeId, Challenge challenge) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
