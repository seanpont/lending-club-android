package com.pontlabs.lendingclub.api;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class ApiError {

    public abstract String message();
    public abstract String path();

    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder message(String message);
        public abstract Builder path(String path);
        public abstract ApiError build();
    }

    public static Builder builder() {
        return new AutoParcel_ApiError.Builder();
    }
}
