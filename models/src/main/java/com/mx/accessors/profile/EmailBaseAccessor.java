package com.mx.accessors.profile;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.profile.Email;

/**
 * Accessor base for profile emails
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/profile/#email-addresses">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/profile/#email-addresses")
public class EmailBaseAccessor extends Accessor {

  public EmailBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create an email
   * @param email
   * @return
   */
  @GatewayAPI
  @API(description = "Create an email address")
  public AccessorResponse<Email> create(Email email) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete email by id
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete an email")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get an email
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get an email address")
  public AccessorResponse<Email> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List emails
   * @return
   */
  @GatewayAPI
  @API(description = "List email addresses")
  public AccessorResponse<MdxList<Email>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update an email
   * @param email
   * @return
   */
  @GatewayAPI
  @API(description = "Update an email address")
  public AccessorResponse<Email> update(String id, Email email) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
