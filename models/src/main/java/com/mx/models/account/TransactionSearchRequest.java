package com.mx.models.account;

/**
 * Contains optional transaction search parameters for MDX OnDemand.
 *
 * Using non-standard naming to support demarshalling in controller. Tried @XmlAttribute, @JsonProperty,
 * and @ConstructorProperties to control the binding names. None of them worked. :.(
 */
@SuppressWarnings({ "checkstyle:MemberName", "checkstyle:ParameterName", "checkstyle:MethodName" })
public class TransactionSearchRequest {
  private Integer page = 1;

  private String start_date;

  public final String getStart_date() {
    return start_date;
  }

  public final void setStart_date(String start_date) {
    this.start_date = start_date;
  }

  public final Integer getPage() {
    return page;
  }

  public final void setPage(Integer page) {
    this.page = page;
  }
}
