package com.mx.models.payout;

import com.mx.models.MdxBase;

public final class Question extends MdxBase<Question> {
  private String answer;
  private String errorMessage;
  private String id;
  private Option[] options;
  private String prompt;
  private String type;

  public Question() {

  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String newAnswer) {
    this.answer = newAnswer;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String newErrorMessage) {
    this.errorMessage = newErrorMessage;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public Option[] getOptions() {
    return options;
  }

  public void setOptions(Option[] newOptions) {
    this.options = newOptions;
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String newPrompt) {
    this.prompt = newPrompt;
  }

  public String getType() {
    return type;
  }

  public void setType(String newType) {
    this.type = newType;
  }
}
