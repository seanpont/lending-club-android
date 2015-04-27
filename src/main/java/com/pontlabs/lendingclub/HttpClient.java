package com.pontlabs.lendingclub;

import com.google.gson.Gson;
import com.pontlabs.lendingclub.api.ApiError;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpClient {

    private static final String BASE_URL = "https://api.lendingclub.com/api/investor/v1/accounts/%s/";
    @Inject OkHttpClient mOkHttpClient;
    @Inject Gson mGson;
    @Inject HttpClient() {}

    public static interface LCCallback<T> {
        void onSuccess(T value);
        void onFailure(ApiError e);
    }

    public <T> void get(final String path, int accountId, String apiKey, Class<T> responseClass, final LCCallback<T> callback) {
        final Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", apiKey)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
                callback.onFailure(ApiError.builder().path(path).message(e.getMessage()).build());
            }

            @Override public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                } else {

                }
            }
        });
    }
}
