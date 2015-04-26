package com.pontlabs.lendingclub;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;

@Singleton
public class LendingClubClient {

    @Inject EventBus mBus;
    @Inject HttpClient mHttpClient;
    @Inject LendingClubData mData;
    @Inject Gson mGson;
    @Inject LendingClubClient() {}

    public boolean hasCredentials() {
        return false;
    }

    public void loadAccountData() {

    }
}
