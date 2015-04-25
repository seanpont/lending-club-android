package com.pontlabs.lendingclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@SuppressWarnings("UnusedDeclaration")
public class LendingClubActivity extends Activity {

    @Inject EventBus mBus;
    @Inject LendingClubClient mClient;
    @Inject LendingClubData mData;

    @InjectView(R.id.sign_in) View mSignInView;
    @InjectView(R.id.loading) View mLoadingView;
    @InjectView(R.id.account) View mAccountView;
    View[] mViews;

    // ===== Activity ==============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LendingClubApplication) getApplication()).inject(this);
        setContentView(R.layout.activity);
        ButterKnife.inject(this);
        mBus.register(this);
        mViews = new View[] {mSignInView, mLoadingView, mAccountView};
        if (!mClient.hasCredentials()) {
            show(mSignInView);
        } else if (mData.hasData()) {
            show(mLoadingView);
            mClient.loadAccountData();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (!mBus.isRegistered(this)) {
            mBus.register(this);
        }
    }

    @Override protected void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    // ===== Views =================================================================================

    private void show(View visibleView) {
        for (View view : mViews) {
            view.setVisibility(view == visibleView ? VISIBLE : GONE);
        }
    }

    // ===== Event Listeners =======================================================================

    public void onEventMainThread(Void v) {}

    // ===== Sign in ===============================================================================

    @InjectView(R.id.sign_in_account_id) EditText mAccountId;
    @InjectView(R.id.sign_in_api_key) EditText mApiKey;
    public void setupSignIn() {

    }


}
