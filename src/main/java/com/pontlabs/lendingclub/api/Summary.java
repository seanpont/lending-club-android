package com.pontlabs.lendingclub.api;

import java.math.BigDecimal;

import auto.parcel.AutoParcel;


@AutoParcel
public abstract class Summary {

    Summary() {}

    public static Builder builder() {
        return new AutoParcel_Summary.Builder();
    }

    @AutoParcel.Builder
    public abstract static class Builder {
        abstract Builder availableCash(BigDecimal availableCash);
        abstract Builder investorId(int investorId);
        abstract Builder accruedInterest(BigDecimal accruedInterest);
        abstract Builder outstandingPrincipal(BigDecimal outstandingPrincipal);
        abstract Builder accountTotal(BigDecimal accountTotal);
        abstract Builder totalNotes(int totalNotes);
        abstract Builder totalPortfolios(int totalPortfolios);
        abstract Builder inFundingBalance(BigDecimal inFundingBalance);
        abstract Builder receivedInterest(BigDecimal receivedInterest);
        abstract Builder receivedPrincipal(BigDecimal receivedPrincipal);
        abstract Builder receivedLateFees(BigDecimal receivedLateFees);
        abstract Summary build();
    }

    public abstract Builder toBuilder();

    public abstract BigDecimal availableCash();
    public abstract int investorId();
    public abstract BigDecimal accruedInterest();
    public abstract BigDecimal outstandingPrincipal();
    public abstract BigDecimal accountTotal();
    public abstract int totalNotes();
    public abstract int totalPortfolios();
    public abstract BigDecimal inFundingBalance();
    public abstract BigDecimal receivedInterest();
    public abstract BigDecimal receivedPrincipal();
    public abstract BigDecimal receivedLateFees();
}