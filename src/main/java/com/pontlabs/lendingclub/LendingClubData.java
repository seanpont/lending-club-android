package com.pontlabs.lendingclub;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pontlabs.lendingclub.api.Credentials;
import com.pontlabs.lendingclub.api.Summary;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LendingClubData {

    @Inject SharedPreferences mPrefs;
    @Inject Gson mGson;
    private Summary mSummary;

    @Inject LendingClubData() {}

    public boolean hasData() {
        return false;
    }

    public void setCredentials(int accountId, String apiKey) {
        throw new RuntimeException("Unimplemented");
    }

    public void setSummary(Summary summary) {
        mSummary = summary;
    }

    public Summary getSummary() {
        return mSummary;
    }

    public void saveCredentials(Credentials credentials) {
        mPrefs.edit().putString("credentials", mGson.toJson(credentials)).apply();
    }

    public Credentials getCredentials() {
        String json = mPrefs.getString("credentials", null);
        return json == null ? null : mGson.fromJson(json, Credentials.class);

    }
}
