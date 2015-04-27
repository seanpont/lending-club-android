package com.pontlabs.lendingclub;

import android.content.SharedPreferences;

import com.pontlabs.lendingclub.api.Credentials;
import com.pontlabs.lendingclub.api.Summary;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LendingClubData {

    @Inject SharedPreferences mPrefs;
    private Summary mSummary;

    @Inject LendingClubData() {}

    public boolean hasData() {
        return false;
    }

    public void saveCredentials(int accountId, String apiKey) {
        throw new RuntimeException("Unimplemented");
    }

    public void setSummary(Summary summary) {
        mSummary = summary;
    }

    public Summary getSummary() {
        return mSummary;
    }

    public void saveCredentials(Credentials credentials) {
        mPrefs.
    }
}
