package com.mx.path.api.lib.realtime.models;

import java.math.BigDecimal;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class MdxAccount {
  @SerializedName("available_balance")
  private BigDecimal availableBalance;
  @SerializedName("account_number")
  private String accountNumber;
  @SerializedName("available_credit")
  private BigDecimal availableCredit;
  @SerializedName("balance")
  private BigDecimal balance;
  @SerializedName("apr")
  private Double apr;
  @SerializedName("apy")
  private Double apy;
  @SerializedName("credit_limit")
  private BigDecimal creditLimit;
  @SerializedName("currency_code")
  private String currencyCode;
  @SerializedName("day_payment_is_due")
  private String dayPaymentIsDue;
  @SerializedName("guid")
  private String guid;
  @SerializedName("has_monthly_transfer_limit")
  private Boolean hasMonthlyTransferLimit;
  @SerializedName("id")
  private String id;
  @SerializedName("interest_rate")
  private Double interestRate;
  @SerializedName("is_closed")
  private Boolean isClosed;
  @SerializedName("is_hidden")
  private Boolean isHidden;
  @SerializedName("last_payment")
  private BigDecimal lastPayment;
  @SerializedName("last_payment_at")
  private Long lastPaymentAt;
  @SerializedName("last_payment_on")
  private String lastPaymentOn;
  @SerializedName("matures_at")
  private Long maturesAt;
  @SerializedName("matures_on")
  private String maturesOn;
  @SerializedName("member_guid")
  private String memberGuid;
  @SerializedName("member_id")
  private String memberId;
  @SerializedName("metadata")
  private String metadata;
  @SerializedName("minimum_balance")
  private BigDecimal minimumBalance;
  @SerializedName("minimum_payment")
  private BigDecimal minimumPayment;
  @SerializedName("monthly_transfer_count")
  private Integer monthlyTransferCount;
  @SerializedName("name")
  private String name;
  @SerializedName("nickname")
  private String nickname;
  @SerializedName("original_balance")
  private BigDecimal originalBalance;
  @SerializedName("payment_due_at")
  private Long paymentDueAt;
  @SerializedName("payment_due_on")
  private String paymentDueOn;
  @SerializedName("payoff_balance")
  private BigDecimal payoffBalance;
  @SerializedName("pending_balance")
  private BigDecimal pendingBalance;
  @SerializedName("routing_transit_number")
  private String routingTransitNumber;
  @SerializedName("started_at")
  private Long startedAt;
  @SerializedName("started_on")
  private String startedOn;
  @SerializedName("statement_balance")
  private BigDecimal statementBalance;
  @SerializedName("subtype")
  private String subtype;
  @SerializedName("type")
  private String type;
}
