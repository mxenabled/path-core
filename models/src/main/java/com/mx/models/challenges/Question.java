package com.mx.models.challenges;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class Question extends MdxBase<Question> {
  private String id;
  private String prompt;
  @SerializedName("prompt_type")
  private String promptType;
  private String info;
  private List<Option> options;
  private String answer;
  @SerializedName("error_message")
  private String errorMessage;
  @SerializedName("is_required")
  private Boolean isRequired;
  @SerializedName("regex_validation")
  private String regexValidation;
  @SerializedName("regex_validation_error_message")
  private String regexValidationErrorMessage;
  @SerializedName("parent_question_id")
  private String parentQuestionId;
  @SerializedName("width")
  private Integer width;
  @SerializedName("button_data")
  private Button buttonData;
  @SerializedName("image_data")
  private Image imageData;
  @SerializedName("text_data")
  private Text textData;
  @SerializedName("camera_data")
  private Camera cameraData;
  @SerializedName("json_data")
  private JsonData jsonData;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPromptType(String promptType) {
    this.promptType = promptType;
  }

  public String getPromptType() {
    return promptType;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getInfo() {
    return info;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

  public List<Option> getOptions() {
    return options;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getAnswer() {
    return answer;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setIsRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }

  public Boolean getIsRequired() {
    return isRequired;
  }

  public void setRegexValidation(String regexValidation) {
    this.regexValidation = regexValidation;
  }

  public String getRegexValidation() {
    return regexValidation;
  }

  public void setRegexValidationErrorMessage(String regexValidationErrorMessage) {
    this.regexValidationErrorMessage = regexValidationErrorMessage;
  }

  public String getRegexValidationErrorMessage() {
    return regexValidationErrorMessage;
  }

  public Boolean getRequired() {
    return isRequired;
  }

  public void setRequired(Boolean required) {
    isRequired = required;
  }

  public String getParentQuestionId() {
    return parentQuestionId;
  }

  public void setParentQuestionId(String parentQuestionId) {
    this.parentQuestionId = parentQuestionId;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Button getButtonData() {
    return buttonData;
  }

  public void setButtonData(Button buttonData) {
    this.buttonData = buttonData;
  }

  public Image getImageData() {
    return imageData;
  }

  public void setImageData(Image imageData) {
    this.imageData = imageData;
  }

  public Text getTextData() {
    return textData;
  }

  public void setTextData(Text textData) {
    this.textData = textData;
  }

  public Camera getCameraData() {
    return cameraData;
  }

  public void setCameraData(Camera cameraData) {
    this.cameraData = cameraData;
  }

  public JsonData getJsonData() {
    return jsonData;
  }

  public void setJsonData(JsonData jsonData) {
    this.jsonData = jsonData;
  }
}
