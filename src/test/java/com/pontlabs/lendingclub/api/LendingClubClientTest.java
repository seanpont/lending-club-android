package com.pontlabs.lendingclub.api;

import com.google.common.util.concurrent.MoreExecutors;
import com.pontlabs.lendingclub.BaseTest;
import com.pontlabs.lendingclub.LendingClubData;
import com.pontlabs.lendingclub.MockHandler;
import com.pontlabs.lendingclub.utils.MockThreadUtils;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.pontlabs.lendingclub.LendingClubApplication.Components;
import static com.pontlabs.lendingclub.api.LendingClubClient.LCCallback;
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
    final CountDownLatch latch = new CountDownLatch(1);
    final LCCallback<Summary> callback = new LendingClubClient.LCCallback<Summary>(Summary.class) {
      @Override public void onSuccess(Summary response) {
        System.out.println(response);
        assertThat(mClient.hasCredentials(), is(true));
        latch.countDown();
      }

      @Override public void onFailure(String message) {
        System.out.println(message);
        Assert.fail();
        latch.countDown();
      }
    };
    mClient.signIn(mAccountId, mApiKey, callback);
    latch.await();
    assertThat(callback, notNullValue());
    assertThat(mClient.hasCredentials(), is(true));
    assertThat(mData.getCredentials(), notNullValue());
  }
}