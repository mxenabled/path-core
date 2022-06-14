package com.mx.models.ondemand.mixins;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mx.models.ondemand.MdxOnDemandLocalDateSerializer;

@JsonRootName("account")
public interface AccountXmlMixin {

  @JsonIgnore
  String getUserId();

  @JsonIgnore
  boolean getWrapped();

  @JsonSerialize(using = MdxOnDemandLocalDateSerializer.class)
  LocalDate getPaymentDueOn();

}
