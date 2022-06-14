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
public final class Transfer extends MdxBase<Transfer> {
  private Double amount;
  private String confirmationId;
  private Long createdAt;
  private LocalDate createdOn;
  @Deprecated
  private String flow;
  @Getter(onMethod_ = { @MdxRelationId(referredClass = Account.class) })
  private String fromAccountId;
  private String repaymentAccountId;
  private String id;
  private String memo;
  private Long postedAt;
  private LocalDate postOn;
  private LocalDate postedOn;
  private String recurringTransferId;
  private String status;
  @Getter(onMethod_ = { @MdxRelationId(referredClass = Account.class) })
  private String toAccountId;
  private String transferType;
  private Long updatedAt;
  private LocalDate updatedOn;

  public Transfer() {
    UserIdProvider.setUserId(this);
  }
}
