package com.mx.models.check;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

/**
 * | Name                  | Type   | Description |
 * | --------------------- | ------ | ----------- |
 * | user_id               | string | The related user ID. |
 * | account_id            | string | The related account by ID. |
 * | transaction_id        | string | The related transaction by ID. |
 * | check_number          | string | The related check number. |
 * | front_image           | string | The image of the front of the check as a [Base64][base64] encoded [Data URI][data_uri] string. |
 * | back_image            | string | The image of the back of the check as a [Base64][base64] encoded [Data URI][data_uri] string. |
 */
public final class CheckImage extends MdxBase<CheckImage> {

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("transaction_id")
  private String transactionId;

  @SerializedName("check_number")
  private String checkNumber;

  @SerializedName("front_image")
  private String frontImage;

  @SerializedName("back_image")
  private String backImage;

  public CheckImage() {
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public void setCheckNumber(String checkNumber) {
    this.checkNumber = checkNumber;
  }

  public String getFrontImage() {
    return frontImage;
  }

  public void setFrontImage(String frontImage) {
    this.frontImage = frontImage;
  }

  public String getBackImage() {
    return backImage;
  }

  public void setBackImage(String backImage) {
    this.backImage = backImage;
  }
}
