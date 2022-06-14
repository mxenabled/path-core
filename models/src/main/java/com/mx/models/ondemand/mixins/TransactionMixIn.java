package com.mx.models.ondemand.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface TransactionMixIn {

  @JsonIgnore
  String getAccountId();

  @JsonIgnore
  String getUserId();

  @JsonIgnore
  boolean getWrapped();

}
