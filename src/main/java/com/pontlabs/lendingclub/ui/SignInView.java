package com.pontlabs.lendingclub.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.pontlabs.lendingclub.R;
import com.pontlabs.lendingclub.utils.ViewUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.pontlabs.lendingclub.LendingClubApplication.Components;
import static com.pontlabs.lendingclub.utils.ObjectUtils.nullToDefault;

public class SignInView extends FrameLayout {

  @InjectView(R.id.sign_in_account_id) public EditText mAccountIdField;
  @InjectView(R.id.sign_in_api_key) public EditText mApiKeyField;
  @InjectView(R.id.email_sign_in_button) public Button mButton;
  @InjectView(R.id.spinner) public View mSpinner;
  Listener mListener = DUMMY_LISTENER;
  ViewUtils mViewUtils;

  public SignInView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this, this);
    mViewUtils = Components.viewUtils();
  }

  @OnClick(R.id.privacy_policy)
  void showPrivacyPolicy() {
    // TODO: TEMPORARY
    mAccountIdField.setText("41482966");
    mApiKeyField.setText("yo9ewo7IZTCHmwEtvWiY3QTILhk=");
  }

  @OnClick(R.id.email_sign_in_button) void signIn() {

    // Reset errors.
    mAccountIdField.setError(null);
    mApiKeyField.setError(null);

    // Store values at the time of the login attempt.
    String accountIdStr = mAccountIdField.getText().toString();
    String apiKey = mApiKeyField.getText().toString();

    boolean cancel = false;
    View focusView = null;

    if (TextUtils.isEmpty(apiKey)) {
      mApiKeyField.setError(getContext().getString(R.string.error_field_required));
      focusView = mApiKeyField;
      cancel = true;
    }

    if (TextUtils.isEmpty(accountIdStr)) {
      mAccountIdField.setError(getContext().getString(R.string.error_field_required));
      focusView = mAccountIdField;
      cancel = true;
    }

    if (!TextUtils.isDigitsOnly(accountIdStr)) {
      mAccountIdField.setError(getContext().getString(R.string.error_accountid_invalid));
      focusView = mAccountIdField;
      cancel = true;
    }

    if (cancel) {
      focusView.requestFocus();
    } else {
      int accountId = Integer.valueOf(accountIdStr);
      mListener.onSignIn(accountId, apiKey);
    }
  }

  // ===== LISTENER ===============================================================================

  public interface Listener {
    void onSignIn(int accountId, String apiKey);
  }

  public void setListener(Listener listener) {
    mListener = nullToDefault(listener, DUMMY_LISTENER);
  }

  static Listener DUMMY_LISTENER = new Listener() {
    @Override public void onSignIn(int accountId, String apiKey) {}
  };

  // ===== RESPONDERS ==============================================================================

  public void setEnabled(boolean enabled) {
    mAccountIdField.setEnabled(enabled);
    mApiKeyField.setEnabled(enabled);
    mButton.setEnabled(enabled);
    mSpinner.setVisibility(enabled ? GONE : VISIBLE);
//    mViewUtils.fade(!enabled, mSpinner);
  }

}
