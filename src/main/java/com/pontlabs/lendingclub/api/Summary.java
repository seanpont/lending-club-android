package com.pontlabs.lendingclub.api;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

import auto.parcel.AutoParcel;
import timber.log.Timber;


@AutoParcel @JsonAdapter(Summary.Adapter.class)
public abstract class Summary {

  Summary() {}

  public static Builder builder() {
    return new AutoParcel_Summary.Builder();
  }

  @AutoParcel.Builder
  public abstract static class Builder {
    public abstract Builder availableCash(BigDecimal availableCash);
    public abstract Builder investorId(int investorId);
    public abstract Builder accruedInterest(BigDecimal accruedInterest);
    public abstract Builder outstandingPrincipal(BigDecimal outstandingPrincipal);
    public abstract Builder accountTotal(BigDecimal accountTotal);
    public abstract Builder totalNotes(int totalNotes);
    public abstract Builder totalPortfolios(int totalPortfolios);
    public abstract Builder infundingBalance(BigDecimal infundingBalance);
    public abstract Builder receivedInterest(BigDecimal receivedInterest);
    public abstract Builder receivedPrincipal(BigDecimal receivedPrincipal);
    public abstract Builder receivedLateFees(BigDecimal receivedLateFees);
    public abstract Summary build();
  }

  public abstract Builder toBuilder();

  public abstract BigDecimal availableCash();
  public abstract int investorId();
  public abstract BigDecimal accruedInterest();
  public abstract BigDecimal outstandingPrincipal();
  public abstract BigDecimal accountTotal();
  public abstract int totalNotes();
  public abstract int totalPortfolios();
  public abstract BigDecimal infundingBalance();
  public abstract BigDecimal receivedInterest();
  public abstract BigDecimal receivedPrincipal();
  public abstract BigDecimal receivedLateFees();


  static class Adapter extends TypeAdapter<Summary> {
    @Override public void write(JsonWriter out, Summary value) throws IOException {
      out.beginObject();
      out.name("availableCash").value(value.availableCash());
      out.name("investorId").value(value.investorId());
      out.name("accruedInterest").value(value.accruedInterest());
      out.name("outstandingPrincipal").value(value.outstandingPrincipal());
      out.name("accountTotal").value(value.accountTotal());
      out.name("totalNotes").value(value.totalNotes());
      out.name("totalPortfolios").value(value.totalPortfolios());
      out.name("infundingBalance").value(value.infundingBalance());
      out.name("receivedInterest").value(value.receivedInterest());
      out.name("receivedPrincipal").value(value.receivedPrincipal());
      out.name("receivedLateFees").value(value.receivedLateFees());
      out.endObject();
    }

    @Override public Summary read(JsonReader in) throws IOException {
      final Builder builder = Summary.builder();
      in.beginObject();
      while (in.hasNext()) {
        final String name = in.nextName();
        switch (name) {
          case "availableCash":
            builder.availableCash(new BigDecimal(in.nextString()));
            break;
          case "investorId":
            builder.investorId(in.nextInt());
            break;
          case "accruedInterest":
            builder.accruedInterest(new BigDecimal(in.nextString()));
            break;
          case "outstandingPrincipal":
            builder.outstandingPrincipal(new BigDecimal(in.nextString()));
            break;
          case "accountTotal":
            builder.accountTotal(new BigDecimal(in.nextString()));
            break;
          case "totalNotes":
            builder.totalNotes(in.nextInt());
            break;
          case "totalPortfolios":
            builder.totalPortfolios(in.nextInt());
            break;
          case "infundingBalance":
            builder.infundingBalance(new BigDecimal(in.nextString()));
            break;
          case "receivedInterest":
            builder.receivedInterest(new BigDecimal(in.nextString()));
            break;
          case "receivedPrincipal":
            builder.receivedPrincipal(new BigDecimal(in.nextString()));
            break;
          case "receivedLateFees":
            builder.receivedLateFees(new BigDecimal(in.nextString()));
            break;
          default:
            Timber.w("unknown key: %s", name);
            break;
        }
      }
      in.endObject();
      return builder.build();
    }
  }
}