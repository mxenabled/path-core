package com.mx.models.ach_transfer;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.mx.models.MdxBase;

@Data
@EqualsAndHashCode(callSuper = true)
public final class AchScheduledTransfer extends MdxBase<AchScheduledTransfer> {
  private String id;
  private String confirmationId;
  private String status;
  private String fromAccountId;
  private String fromAchAccountId;
  private String toAccountId;
  private String toAchAccountId;
  private BigDecimal amount;
  private String memo;
  private String frequencyId;
  private LocalDate startOn;
  private Long endAfterCount;
  private Long createdAt;
  private String transferType;
}
