package com.mx.models.payout;

import com.mx.models.MdxBase;

public final class ChallengeAnswer extends MdxBase<ChallengeAnswer> {

  private String answer;
  private String id;

  public ChallengeAnswer() {

  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String newAnswer) {
    this.answer = newAnswer;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

}
