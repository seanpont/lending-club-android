package com.pontlabs.lendingclub.api;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonAdapter(Credentials.Adapter.class)
public abstract class Credentials {

    public abstract int accountId();
    public abstract String apiKey();

    public static Builder builder() {
        return new AutoParcel_Credentials.Builder();
    }

    @AutoParcel.Builder
    public interface Builder {
        public Builder accountId(int accountId);
        public Builder apiKey(String apiKey);
        public Credentials build();
    }

    public static class Adapter extends TypeAdapter<Credentials> {

        @Override public void write(JsonWriter out, Credentials credentials) throws IOException {
            // implement write: combine firstName and lastName into name
            out.beginObject();
            out.name("accountId").value(credentials.accountId());
            out.name("apiKey").value(credentials.apiKey());
            out.endObject();
        }

        @Override public Credentials read(JsonReader in) throws IOException {
            Builder builder = Credentials.builder();
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "accountId":
                        builder.accountId(in.nextInt());
                        break;
                    case "apiKey":
                        builder.apiKey(in.nextString());
                        break;
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}
