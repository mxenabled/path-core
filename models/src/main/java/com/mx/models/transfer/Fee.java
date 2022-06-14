package com.mx.models.transfer;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public final class Fee extends MdxBase<Fee> {

  private Double amount;
  private String description;
}
