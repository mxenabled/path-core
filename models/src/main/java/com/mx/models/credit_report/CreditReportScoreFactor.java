package com.mx.models.credit_report;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

/**
 * To provide a list of the key Factors effecting your credit report.
 * Note that score factors are considered Read Only therefore they do not contain options to update or delete.
 */
public final class CreditReportScoreFactor extends MdxBase<CreditReportScoreFactor> {
  @SerializedName("description")
  private String description;
  @SerializedName("id")
  private String id;
  @SerializedName("keep_in_mind")
  private String keepInMind;
  @SerializedName("title")
  private String title;

  public CreditReportScoreFactor() {
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKeepInMind() {
    return keepInMind;
  }

  public void setKeepInMind(String keepInMind) {
    this.keepInMind = keepInMind;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
