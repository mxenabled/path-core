package com.mx.models.id;

import java.util.List;

import com.mx.models.MdxBase;
import com.mx.models.authorization.Authorization;

public final class MfaChallenge extends MdxBase<MfaChallenge> {

  private String answer;
  private String errorMessage;
  private String id;
  private List<MfaChallengeOption> options;
  private String question;
  private List<MfaChallengeQuestion> questions;
  private String type;
  private Authorization authorization;

  public MfaChallenge() {

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

  public List<MfaChallengeOption> getOptions() {
    return options;
  }

  public void setOptions(List<MfaChallengeOption> options) {
    this.options = options;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String newQuestion) {
    this.question = newQuestion;
  }

  public List<MfaChallengeQuestion> getQuestions() {
    return questions;
  }

  public void setQuestions(List<MfaChallengeQuestion> questions) {
    this.questions = questions;
  }

  public String getType() {
    return type;
  }

  public void setType(String newType) {
    this.type = newType;
  }

  public Authorization getAuthorization() {
    return authorization;
  }

  public void setAuthorization(Authorization authorization) {
    this.authorization = authorization;
  }
}
