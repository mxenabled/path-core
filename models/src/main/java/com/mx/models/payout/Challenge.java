package com.mx.models.payout;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class Challenge extends MdxBase<Challenge> {

  private String id;
  private String prompt;
  private Question[] questions;

  public Challenge() {
    UserIdProvider.setUserId(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String newPrompt) {
    this.prompt = newPrompt;
  }

  public Question[] getQuestions() {
    return questions;
  }

  public void setQuestions(Question[] newQuestions) {
    this.questions = newQuestions;
  }
}
