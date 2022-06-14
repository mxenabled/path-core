package com.mx.models.challenges;

import java.util.List;

import com.mx.models.MdxBase;

public final class Challenge extends MdxBase<Challenge> {
  private String id;
  private String prompt;
  private String title;
  private List<Question> questions;
  private String[] modes;
  private String format;
  private List<Action> actions;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public String[] getModes() {
    return modes;
  }

  public void setModes(String[] modes) {
    this.modes = modes;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public List<Action> getActions() {
    return actions;
  }

  public void setActions(List<Action> actions) {
    this.actions = actions;
  }
}
