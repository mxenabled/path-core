package com.mx.models.ach_transfer;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;
import com.mx.models.challenges.Challenge;

@Deprecated // This is being replaced by: https://developer.mx.com/drafts/mdx/ach_transfer/#mdx-ach-transfer
public final class Transfer extends MdxBase<Transfer> {

  private String id;
  private String status;
  @SerializedName("funding_source_id")
  private String fundingSourceId;
  @SerializedName("funding_destination_id")
  private String fundingDestinationId;
  private Double amount;
  @SerializedName("created_at")
  private Long createdAt;
  private List<Challenge> challenges;

  public Transfer() {
    UserIdProvider.setUserId(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFundingSourceId() {
    return fundingSourceId;
  }

  public void setFundingSourceId(String fundingSourceId) {
    this.fundingSourceId = fundingSourceId;
  }

  public String getFundingDestinationId() {
    return fundingDestinationId;
  }

  public void setFundingDestinationId(String fundingDestinationId) {
    this.fundingDestinationId = fundingDestinationId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public List<Challenge> getChallenges() {
    return challenges;
  }

  public void setChallenges(List<Challenge> newChallenges) {
    this.challenges = newChallenges;
  }
}
