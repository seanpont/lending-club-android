package com.pontlabs.lendingclub.api;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class Credentials {

    Credentials() {}

    public abstract int accountId();
    public abstract String apiKey();

    public static Credentials create(int accountId, String apiKey) {
        return new AutoParcel_Credentials(accountId, apiKey);
    }

}
