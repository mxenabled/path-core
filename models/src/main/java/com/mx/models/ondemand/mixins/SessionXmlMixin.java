package com.mx.models.ondemand.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("session")
public interface SessionXmlMixin {
  @JsonIgnore
  boolean getWrapped();

  @JsonIgnore
  boolean getUserId();

  @JsonProperty("key") // Rename id to key for MDXv5
  String getId();
}
