package com.mx.models.account;

import java.util.List;

import com.mx.models.MdxBase;
import com.mx.models.MdxList;

/**
 * Represents a pageable list of transactions.
 */
public class TransactionsPage extends MdxBase<TransactionsPage> {
  private Integer pages = 1;
  private Integer page = 1;
  private String startDate;
  private MdxList<Transaction> transactions;

  public TransactionsPage() {
  }

  public TransactionsPage(MdxList<Transaction> transactions) {
    this();
    this.transactions = transactions;
  }

  public final Integer getPage() {
    return page;
  }

  public final void setPage(Integer page) {
    this.page = page;
  }

  public final Integer getPages() {
    return pages;
  }

  public final void setPages(Integer pages) {
    this.pages = pages;
  }

  public final String getStartDate() {
    return startDate;
  }

  public final void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public final List<Transaction> getTransactions() {
    return transactions;
  }

  public final void setTransactions(MdxList<Transaction> transactions) {
    this.transactions = transactions;
  }
}
