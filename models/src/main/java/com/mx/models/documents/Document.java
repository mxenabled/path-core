package com.mx.models.documents;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

/*
 Up to date with MDX Documents Rev 5
 https://developer.mx.com/drafts/mdx/documents/index.html#changes
 */
public final class Document extends MdxBase<Document> {

  private String accountId;
  private LocalDate createdOn;
  private String fileData;
  private String fileName;
  private Integer fileSize;
  private String fileType;
  private String id;
  private String name;
  private String type;

  public Document() {
    UserIdProvider.setUserId(this);
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDate createdOn) {
    this.createdOn = createdOn;
  }

  public String getFileData() {
    return fileData;
  }

  public void setFileData(String newFileData) {
    this.fileData = newFileData;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String newFileName) {
    this.fileName = newFileName;
  }

  public Integer getFileSize() {
    return fileSize;
  }

  public void setFileSize(Integer newFileSize) {
    this.fileSize = newFileSize;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String newFileType) {
    this.fileType = newFileType;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
