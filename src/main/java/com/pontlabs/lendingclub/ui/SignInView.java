package com.pontlabs.lendingclub.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.pontlabs.lendingclub.R;
import com.pontlabs.lendingclub.api.LendingClubClient;
import com.pontlabs.lendingclub.utils.ViewUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.pontlabs.lendingclub.LendingClubApplication.Components;

public class SignInView extends FrameLayout {

    @InjectView(R.id.sign_in_account_id) public EditText mAccountIdField;
    @InjectView(R.id.sign_in_api_key) public EditText mApiKeyField;
    @InjectView(R.id.email_sign_in_button) public Button mButton;
    @InjectView(R.id.spinner) public View mSpinner;
    private boolean mAuthenticating;
    private LendingClubClient mClient;
    private ViewUtils mViewUtils;

    public SignInView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this, this);
        mClient = Components.lendingClubClient();
        mViewUtils = Components.viewUtils();
    }

    @OnClick(R.id.email_sign_in_button) void signIn() {
        if (mAuthenticating) {
            return;
        }

        // Reset errors.
        mAccountIdField.setError(null);
        mApiKeyField.setError(null);

        // Store values at the time of the login attempt.
        String accountIdStr = mAccountIdField.getText().toString();
        String apiKey = mApiKeyField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(apiKey)) {
            mApiKeyField.setError(getContext().getString(R.string.error_field_required));
            focusView = mApiKeyField;
            cancel = true;
        }
        if (!TextUtils.isEmpty(accountIdStr)) {
            mAccountIdField.setError(getContext().getString(R.string.error_field_required));
            focusView = mAccountIdField;
            cancel = true;
        }

        int accountId = 0;
        try {
            accountId = Integer.valueOf(accountIdStr);
        } catch (NumberFormatException e) {
            mAccountIdField.setError(getContext().getString(R.string.error_accountid_invalid));
            focusView = mAccountIdField;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthenticating = true;
            showProgress(true);
            mClient.signIn(accountId, apiKey);
        }
    }

    private void showProgress(boolean show) {
        mViewUtils.fade(show, mSpinner);
        mButton.setEnabled(!show);
    }

}
