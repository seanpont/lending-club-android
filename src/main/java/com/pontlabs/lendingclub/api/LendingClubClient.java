package com.pontlabs.lendingclub.api;

import com.pontlabs.lendingclub.HttpClient;
import com.pontlabs.lendingclub.LendingClubData;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

import static com.pontlabs.lendingclub.HttpClient.LCCallback;

@Singleton
public class LendingClubClient {

    @Inject EventBus mBus;
    @Inject HttpClient mHttpClient;
    @Inject LendingClubData mData;
    @Inject LendingClubClient() {}

    public boolean hasCredentials() {
        return false;
    }

    public void loadAccountData() {

    }

    public void signIn(final int accountId, final String apiKey) {
        Timber.i("signIn %s %s", accountId, apiKey);
        mHttpClient.get("summary", accountId, apiKey, Summary.class, new LCCallback<Summary>() {
            @Override public void onSuccess(Summary value) {
                mData.saveCredentials(Credentials.builder().accountId(accountId).apiKey(apiKey).build());
                mData.setSummary(value);
            }

            @Override public void onFailure(ApiError e) {

            }
        });
    }


    public void setHttpClient(HttpClient httpClient) {
        mHttpClient = httpClient;
    }
}
