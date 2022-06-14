package com.mx.models.credit_report;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class CreditReport extends MdxBase<CreditReport> {
  @SerializedName("agency")
  private ConsumerReportingAgencies agency;
  @SerializedName("id")
  private String id;
  @SerializedName("pulled_on")
  private LocalDate pulledOn;
  @SerializedName("score")
  private Integer score;
  @SerializedName("score_version")
  private ScoreVersions scoreVersion;

  public CreditReport() {
  }

  public ConsumerReportingAgencies getAgency() {
    return agency;
  }

  public void setAgency(ConsumerReportingAgencies agency) {
    this.agency = agency;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDate getPulledOn() {
    return pulledOn;
  }

  public void setPulledOn(LocalDate pulledOn) {
    this.pulledOn = pulledOn;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public ScoreVersions getScoreVersion() {
    return scoreVersion;
  }

  public void setScoreVersion(ScoreVersions scoreVersion) {
    this.scoreVersion = scoreVersion;
  }

  public enum ConsumerReportingAgencies {
    EXPERIAN("Experian"), EQUIFAX("Equifax"), TRANS_UNION("TransUnion");

    private String description;

    public String getDescription() {
      return description;
    }

    ConsumerReportingAgencies(String description) {
      this.description = description;
    }
  }

  public enum ScoreVersions {
    FICO_SCORE_8("Fico Score 8"), FICO_SCORE_9("Fico Score 9");

    private String description;

    public String getDescription() {
      return description;
    }

    ScoreVersions(String description) {
      this.description = description;
    }
  }
}
