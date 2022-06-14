package com.mx.models.ach_transfer;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;

import com.mx.models.MdxBase;
import com.mx.models.MdxRelationId;
import com.mx.models.account.Account;

@Data
public final class AchTransfer extends MdxBase<AchTransfer> {
  private String id;
  private String achScheduledTransferId;
  private Double amount;
  private Long createdAt;
  @Getter(onMethod_ = { @MdxRelationId(referredClass = Account.class) })
  private String fromAccountId;
  private String fromAchAccountId;
  private String memo;
  private LocalDate processedOn;
  private String status;
  @Getter(onMethod_ = { @MdxRelationId(referredClass = Account.class) })
  private String toAccountId;
  private String toAchAccountId;
  private String transferType;
}
