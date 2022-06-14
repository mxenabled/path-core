package com.mx.models.challenges;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class Button extends MdxBase<Button> {
  private String type;
  private List<Action> actions;
  @SerializedName("requires_valid_answers")
  private Boolean requiresValidAnswers;
  @SerializedName("disabled_duration")
  private Integer disabledDurationSeconds;
  @SerializedName("is_submit_button")
  private Boolean isSubmitButton;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Action> getActions() {
    return actions;
  }

  public void setActions(List<Action> actions) {
    this.actions = actions;
  }

  public Boolean getRequiresValidAnswers() {
    return requiresValidAnswers;
  }

  public void setRequiresValidAnswers(Boolean requiresValidAnswers) {
    this.requiresValidAnswers = requiresValidAnswers;
  }

  public Integer getDisabledDurationSeconds() {
    return disabledDurationSeconds;
  }

  public void setDisabledDurationSeconds(Integer disabledDurationSeconds) {
    this.disabledDurationSeconds = disabledDurationSeconds;
  }

  public Boolean getIsSubmitButton() {
    return isSubmitButton;
  }

  public void setIsSubmitButton(Boolean isSubmitButton) {
    this.isSubmitButton = isSubmitButton;
  }

}
