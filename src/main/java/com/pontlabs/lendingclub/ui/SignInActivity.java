package com.pontlabs.lendingclub.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pontlabs.lendingclub.R;
import com.pontlabs.lendingclub.api.LendingClubClient;
import com.pontlabs.lendingclub.api.Summary;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.pontlabs.lendingclub.LendingClubApplication.Components;

public class SignInActivity extends Activity implements SignInView.Listener {

  @Inject LendingClubClient mClient;
  @InjectView(R.id.sign_in) SignInView mSignInView;
  @InjectView(R.id.loading) View mLoadingView;
  boolean mPaused = true;

  // ===== Activity ==============================================================================

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity);
    mClient = Components.lendingClubClient();
    ButterKnife.inject(this);
    show(mLoadingView);
  }

  @Override protected void onResume() {
    super.onResume();
    mPaused = false;

    mSignInView.setListener(this);

    if (mClient.hasCredentials()) {
      show(mLoadingView);
      mClient.getSummary(mSummaryCallback); // fetch summary and also test credentials
    } else {
      show(mSignInView);
      mSignInView.setEnabled(true);
    }
  }

  @Override protected void onPause() {
    super.onPause();
    mPaused = true;

    mSignInView.setListener(null);
  }

  // ===== Views =================================================================================

  private void show(View visibleView) {
    mSignInView.setVisibility(mSignInView == visibleView ? VISIBLE : GONE);
    mLoadingView.setVisibility(mLoadingView == visibleView ? VISIBLE : GONE);
  }

  // ===== SignInView.Listener =======================================================================

  @Override public void onSignIn(int accountId, String apiKey) {
    mClient.signIn(accountId, apiKey, mSummaryCallback);
  }

  // ===== LCCallback ===============================================================================

  private LendingClubClient.ClubCallback<Summary> mSummaryCallback = new LendingClubClient.ClubCallback<Summary>() {

    @Override public void onSuccess(Summary response) {
      if (mPaused) return;
      startActivity(new Intent(SignInActivity.this, AccountActivity.class));
      finish();
    }

    @Override public void onFailure(String message) {
      if (mPaused) return;
      Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG);
      show(mSignInView);
      mSignInView.setEnabled(true);
    }
  };
}
