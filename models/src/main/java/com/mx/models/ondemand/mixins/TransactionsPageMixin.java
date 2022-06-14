package com.mx.models.ondemand.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mx.models.MdxList;
import com.mx.models.account.Transaction;

public interface TransactionsPageMixin {

  @JacksonXmlProperty(isAttribute = true)
  String getPage();

  @JacksonXmlProperty(isAttribute = true)
  String getPages();

  @JacksonXmlProperty(isAttribute = true)
  String getStartDate();

  @JacksonXmlElementWrapper(useWrapping = false)
  @JsonProperty("transaction")
  MdxList<Transaction> getTransactions();

  @JsonIgnore
  boolean getWrapped();

}
