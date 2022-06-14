package com.mx.models.ondemand.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("account_numbers")
public interface AccountNumberXmlMixin {
  @JsonIgnore
  boolean getWrapped();
}
