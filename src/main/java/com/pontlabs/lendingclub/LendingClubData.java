package com.pontlabs.lendingclub;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LendingClubData {

    @Inject SharedPreferences mPrefs;
    @Inject LendingClubData() {}

    public boolean hasData() {
        return false;
    }
}
