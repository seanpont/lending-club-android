package com.pontlabs.lendingclub.ui;

import android.view.View;

import com.pontlabs.lendingclub.BaseTest;
import com.pontlabs.lendingclub.MockHttpClient;
import com.pontlabs.lendingclub.api.LendingClubClient;
import com.pontlabs.lendingclub.api.Summary;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.pontlabs.lendingclub.Matchers.isGone;
import static com.pontlabs.lendingclub.Matchers.isVisible;
import static com.pontlabs.lendingclub.MockHttpClient.RequestCallback;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class SignInActivityTest extends BaseTest {

  private SignInActivity mActivity;

  @Override public void setup() {
    super.setup();
    mActivity = Robolectric.setupActivity(SignInActivity.class);
  }

  @Test
  public void test_sign_in_form_validation() throws Exception {
    {
      assertThat(mActivity.mSignInView, isVisible());
      assertThat(mActivity.mLoadingView, isGone());
      assertThat(mActivity.mSignInView.mAccountIdField.getError(), nullValue());
      assertThat(mActivity.mSignInView.mApiKeyField.getError(), nullValue());
    }
    mActivity.mSignInView.mApiKeyField.setText("apiKey");
    mActivity.mSignInView.mButton.performClick();
    {
      assertThat(mActivity.mSignInView.mAccountIdField.getError(), notNullValue());
      assertThat(mActivity.mSignInView.mAccountIdField.hasFocus(), is(true));
      assertThat(mActivity.mSignInView.mApiKeyField.getError(), nullValue());
    }
  }

  @Test
  public void test_sign_in_calls_api() throws Exception {
    mActivity.mSignInView.mAccountIdField.setText("1234");
    mActivity.mSignInView.mApiKeyField.setText("apiKey");
    mActivity.mSignInView.mButton.performClick();
    final RequestCallback requestCallback = mHttpClient.popRequest();
    {
      assertThat(requestCallback.mRequest.urlString(), containsString("summary"));
    }
    Response response = new Response.Builder()
        .body(ResponseBody.create(LendingClubClient.JSON, ""))
        .code(200)
        .build();
    requestCallback.mCallback.onResponse(response);

  }


}