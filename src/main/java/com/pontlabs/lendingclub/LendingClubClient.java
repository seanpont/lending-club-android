package com.pontlabs.lendingclub;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class LendingClubClient {

    @Inject EventBus mBus;
    @Inject OkHttpClient mHttpClient;

    @Inject
    public LendingClubClient() {


    }

    public boolean hasCredentials() {
        return false;
    }

    public void loadAccountData() {

    }
}
