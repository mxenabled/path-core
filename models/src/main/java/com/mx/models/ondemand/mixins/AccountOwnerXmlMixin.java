package com.mx.models.ondemand.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("account")
public interface AccountOwnerXmlMixin {
  @JsonIgnore
  boolean getWrapped();
}
