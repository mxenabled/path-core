package com.mx.accessors.profile;

import lombok.AccessLevel;
import lombok.Getter;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.challenges.Challenge;
import com.mx.models.profile.Password;
import com.mx.models.profile.Profile;
import com.mx.models.profile.UserName;

/**
 * Accessor base for profiles
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/profile/#profile">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/profile/#profile")
public class ProfileBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AddressBaseAccessor addresses;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private EmailBaseAccessor emails;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private PhoneBaseAccessor phones;

  public ProfileBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Accessor for address operations
   * @return accessor
   */
  @API(specificationUrl = "https://developer.mx.com/drafts/mdx/profile/#addresses")
  public AddressBaseAccessor addresses() {
    if (addresses != null) {
      return addresses;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set addresses accessor
   * @param addresses
   */
  public void setAddresses(AddressBaseAccessor addresses) {
    this.addresses = addresses;
  }

  /**
   * Accessor for email operations
   * @return accessor
   */
  @API(specificationUrl = "https://developer.mx.com/drafts/mdx/profile/#addresses")
  public EmailBaseAccessor emails() {
    if (emails != null) {
      return emails;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set emails accessor
   * @param emails
   */
  public void setEmails(EmailBaseAccessor emails) {
    this.emails = emails;
  }

  /**
   * Accessor for phone operations
   * @return accessor
   */
  @API(specificationUrl = "https://developer.mx.com/drafts/mdx/profile/#addresses")
  public PhoneBaseAccessor phones() {
    if (phones != null) {
      return phones;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * set ohones accessor
   * @param phones
   */
  public void setPhones(PhoneBaseAccessor phones) {
    this.phones = phones;
  }

  /**
   * Get user's profile
   * @return
   */
  @GatewayAPI
  @API(description = "Get user's profile")
  public AccessorResponse<Profile> get() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update user's profile
   * @param profile
   * @return
   */
  @GatewayAPI
  @API(description = "Update user's profile")
  public AccessorResponse<Profile> update(Profile profile) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update user's user name
   * @param userName
   * @return
   */
  @GatewayAPI
  @API(description = "Update user's user name")
  public AccessorResponse<Void> updateUserName(UserName userName) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update user's password
   * @param password
   * @return
   */
  @GatewayAPI
  @API(description = "Update user's password")
  public AccessorResponse<MdxList<Challenge>> updatePassword(Password password) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update user's password
   * @param challengeId
   * @param challenge
   * @return
   */
  @GatewayAPI
  @API(description = "Update user's password")
  public AccessorResponse<MdxList<Challenge>> updatePasswordResume(String challengeId, Challenge challenge) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
