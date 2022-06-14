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
import com.mx.models.profile.Phone;

/**
 * Accessor base for profile phones
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/profile/#phone-numbers">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/profile/#phone-numbers")
public class PhoneBaseAccessor extends Accessor {

  public PhoneBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a phone
   * @param phone
   * @return
   */
  @GatewayAPI
  @API(description = "Create a phone")
  public AccessorResponse<Phone> create(Phone phone) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete phone by id
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete a phone")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a phone
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a phone")
  public AccessorResponse<Phone> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List phones
   * @return
   */
  @GatewayAPI
  @API(description = "List phones")
  public AccessorResponse<MdxList<Phone>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a phone
   * @param phone
   * @return
   */
  @GatewayAPI
  @API(description = "Update a phone")
  public AccessorResponse<Phone> update(String id, Phone phone) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
