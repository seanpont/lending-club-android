package com.pontlabs.lendingclub;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpClient {

    @Inject OkHttpClient mOkHttpClient;
    @Inject HttpClient() {}

}
