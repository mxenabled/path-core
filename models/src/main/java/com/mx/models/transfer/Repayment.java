package com.mx.models.transfer;

import java.time.LocalDate;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public final class Repayment extends MdxBase<Repayment> {

  private Double balance;
  private Double interest;
  private Double otherFeeAmount;
  private String otherFeeDescription;
  private LocalDate postOn;
  private Double principal;
  private Double total;
}
