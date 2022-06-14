package com.mx.models.challenges;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.mx.models.MdxBase;

@Data
@EqualsAndHashCode(callSuper = true)
public final class JsonData extends MdxBase<JsonData> {
  private String jsonType;
}
