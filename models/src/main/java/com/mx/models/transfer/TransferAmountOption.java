package com.mx.models.transfer;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.mx.models.MdxBase;

/**
 * Used in creating transfers and recurring transfers based on specific amounts
 * related to the source and destination accounts. The typical use for this is to
 * provide easier amount entry for the user by selecting an amount rather than
 * having to manually enter the value.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransferAmountOption extends MdxBase<TransferAmountOption> {
  private String id;
  private String name;
  private String description;
  private BigDecimal amount;
  private Boolean isRecurringEnabled;
}
