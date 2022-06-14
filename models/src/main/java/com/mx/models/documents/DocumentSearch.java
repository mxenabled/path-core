package com.mx.models.documents;

/**
 * Used to pass all search attributes to document search function.
 * Binds to incoming document search query string parameters
 */
@SuppressWarnings({ "checkstyle:MemberName", "checkstyle:ParameterName", "checkstyle:MethodName" })
public class DocumentSearch {

  private String end_on;
  private String start_on;
  private String type;
  private String account_id;

  // NOTE: Non standard naming to assist with parameter binding

  public final String getEnd_on() {
    return end_on;
  }

  public final void setEnd_on(String end_on) {
    this.end_on = end_on;
  }

  public final String getStart_on() {
    return start_on;
  }

  public final void setStart_on(String start_on) {
    this.start_on = start_on;
  }

  public final String getType() {
    return type;
  }

  public final void setType(String type) {
    this.type = type;
  }

  public final String getAccount_id() {
    return account_id;
  }

  public final void setAccount_id(String account_id) {
    this.account_id = account_id;
  }
}
