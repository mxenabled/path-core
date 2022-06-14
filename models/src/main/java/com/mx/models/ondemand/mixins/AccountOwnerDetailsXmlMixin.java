package com.mx.models.ondemand.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("account_owner")
public interface AccountOwnerDetailsXmlMixin {
  @JsonIgnore
  boolean getWrapped();
}
