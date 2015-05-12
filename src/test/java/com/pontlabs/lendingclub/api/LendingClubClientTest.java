package com.pontlabs.lendingclub.api;

import com.google.common.util.concurrent.MoreExecutors;
import com.pontlabs.lendingclub.LendingClubData;
import com.pontlabs.lendingclub.utils.MockThreadUtils;
import com.squareup.okhttp.Dispatcher;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.pontlabs.lendingclub.LendingClubApplication.Components;
import static com.pontlabs.lendingclub.api.LendingClubClient.ClubCallback;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class LendingClubClientTest {

  private LendingClubClient mClient;
  private LendingClubData mData;
  private int mAccountId;
  private String mApiKey;


  @Before
  public void setup() throws IOException {
    mClient = Components.lendingClubClient();
    mData = Components.lendingClubData();
    mClient.setThreadUtils(new MockThreadUtils());
    mClient.mOkHttpClient.setDispatcher(new Dispatcher(MoreExecutors.newDirectExecutorService()));

    final Properties properties = new Properties();
    properties.load(new FileInputStream("credentials.properties"));
    mAccountId = Integer.valueOf(properties.getProperty("account_id"));
    mApiKey = properties.getProperty("api_key");
  }


  @Test
  public void testSummary() throws Exception {
    mClient.signIn(mAccountId, mApiKey, new ClubCallback<Summary>() {
      @Override public void onSuccess(Summary response) {
        System.out.println(response);
        assertThat(mClient.hasCredentials(), is(true));
      }

      @Override public void onFailure(String message) {
        System.out.println(message);
        Assert.fail();
      }
    });
    assertThat(mClient.hasCredentials(), is(true));
    assertThat(mData.getCredentials(), notNullValue());
  }
}