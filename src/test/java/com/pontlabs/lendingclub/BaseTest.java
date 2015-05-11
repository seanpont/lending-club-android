package com.pontlabs.lendingclub;

import com.pontlabs.lendingclub.api.LendingClubClient;
import com.pontlabs.lendingclub.utils.MockThreadUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.pontlabs.lendingclub.LendingClubApplication.Components;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class BaseTest {

  protected LendingClubApplication mApplication;
  protected LendingClubClient mClient;
  protected LendingClubData mData;
  protected MockHttpClient mHttpClient;

  @Before
  public void setup() {
    mApplication = (LendingClubApplication) Robolectric.application;
    mClient = Components.lendingClubClient();
    mData = Components.lendingClubData();
    mHttpClient = new MockHttpClient();
    mClient.setHttpClient(mHttpClient);
    mClient.setThreadUtils(new MockThreadUtils());
  }

  @Test
  public void testApplication() {
    assertThat(mApplication, notNullValue());
  }

}
