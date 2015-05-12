package com.pontlabs.lendingclub.api;

import com.google.gson.annotations.JsonAdapter;

import java.math.BigDecimal;
import java.util.Date;

import auto.parcel.AutoParcel;

/**
 * https://api.lendingclub.com/api/investor/<version>/accounts/<investor id>/detailednotes
 */
@AutoParcel
@JsonAdapter(Credentials.Adapter.class)
public abstract class DetailedNote {

  public abstract long loanId();
  public abstract long noteId();
  public abstract long orderId();
  public abstract String purpose();
  public abstract BigDecimal interestRate();
  public abstract int loanLength();
  public abstract String loanStatus();
  public abstract String grade();
  public abstract String currentPaymentStatus();
  public abstract boolean canBeTraded();
  public abstract String creditTrend();
  public abstract BigDecimal loanAmount();
  public abstract BigDecimal noteAmount();
  public abstract BigDecimal paymentsReceived();
  public abstract BigDecimal accruedInterest();
  public abstract BigDecimal principalPending();
  public abstract BigDecimal interestPending();
  public abstract BigDecimal principalReceived();
  public abstract BigDecimal interestReceived();
  public abstract Date nextPaymentDate();
  public abstract Date issueDate();
  public abstract Date orderDate();
  public abstract Date loanStatusDate();
}
