package com.mx.models.transfer;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.mx.models.MdxBase;
import com.mx.models.MdxRelationId;
import com.mx.models.UserIdProvider;
import com.mx.models.account.Account;

@EqualsAndHashCode(callSuper = true)
@Data
public final class RecurringTransfer extends MdxBase<RecurringTransfer> {
  private Double amount;
  private String confirmationId;
  private Double endAfterAmount;
  private Integer endAfterCount;
  private LocalDate endOn;
  @Deprecated
  private String flow;
  private String frequencyId;
  @Getter(onMethod_ = { @MdxRelationId(referredClass = Account.class) })
  private String fromAccountId;
  private String id;
  private LocalDate lastTransferOn;
  private String memo;
  private LocalDate nextTransferOn;
  private LocalDate startOn;
  private String status;
  @Getter(onMethod_ = { @MdxRelationId(referredClass = Account.class) })
  private String toAccountId;
  private String transferType;

  public RecurringTransfer() {
    UserIdProvider.setUserId(this);
  }
}
