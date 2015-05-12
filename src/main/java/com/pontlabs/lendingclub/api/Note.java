package com.pontlabs.lendingclub.api;

import auto.parcel.AutoParcel;

/**
 * https://api.lendingclub.com/api/investor/<version>/accounts/<investor id>/notes
 */
@AutoParcel
public abstract class Note {
  public abstract String loanId();
  public abstract String noteId();
  public abstract String orderId();
  public abstract String interestRate();
  public abstract String loanLength();
  public abstract String loanStatus();
  public abstract String grade();
  public abstract String loanAmount();
  public abstract String noteAmount();
  public abstract String paymentsReceived();
  public abstract String issueDate();
  public abstract String orderDate();
  public abstract String loanStatusDate();
}
