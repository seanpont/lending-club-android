package com.pontlabs.lendingclub;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.pontlabs.lendingclub.LendingClubApplication.Components;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class BaseTest {

    protected LendingClubApplication mApplication;
    protected LendingClubClient mClient;
    protected LendingClubData mData;
    protected MockHttpClient mHttpClient;
    protected LendingClubActivity mActivity;

    @Before
    public void setup() {
        mApplication = (LendingClubApplication) Robolectric.application;
        mClient = Components.lendingClubClient();
        mData = Components.lendingClubData();
        mHttpClient = new MockHttpClient();
        mClient.mHttpClient = mHttpClient;

        mActivity = Robolectric.setupActivity(LendingClubActivity.class);
    }

}
