package com.mx.accessors.managed_card;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.managed_cards.ManagedCard;

/**
 * Accessor base for managed card operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/managed_cards/#mdx-managed-cards">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/managed_cards/#mdx-managed-cards")
public abstract class ManagedCardBaseAccessor extends Accessor {

  public ManagedCardBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Activate a managed card
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Activate managed card")
  public AccessorResponse<ManagedCard> activate(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create a managed card
   * @param card
   * @return
   */
  @GatewayAPI
  @API(description = "Create managed card")
  public AccessorResponse<ManagedCard> create(ManagedCard card) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * @deprecated - Use {@link #pause(String)} instead
   */
  @GatewayAPI
  @API(description = "Disable managed card (deprecated)")
  @Deprecated
  public AccessorResponse<Void> disable(String id) {
    return pause(id);
  }

  /**
   * Enable a managed card
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Enable managed card")
  public AccessorResponse<Void> enable(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a managed card
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get managed card")
  public AccessorResponse<ManagedCard> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get unmasked card number
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get unmasked card number")
  public AccessorResponse<ManagedCard> getUnmaskedCardNumber(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get cvv
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get unmasked CVV")
  public AccessorResponse<ManagedCard> getCvv(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List managed cards
   * @return
   */
  @GatewayAPI
  @API(description = "List managed cards")
  public AccessorResponse<MdxList<ManagedCard>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Pause a managed card
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Pause managed card")
  public AccessorResponse<Void> pause(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Replace a managed card
   * @return
   */
  @GatewayAPI
  @API(description = "Replace a managed card")
  public AccessorResponse<ManagedCard> replace(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set managed card pin
   * @return
   */
  @GatewayAPI
  @API(description = "Set managed card pin")
  public AccessorResponse<ManagedCard> setPin(String id, ManagedCard card) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a managed card
   * @return
   */
  @GatewayAPI
  @API(description = "Update a managed card")
  public AccessorResponse<ManagedCard> update(String id, ManagedCard card) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
