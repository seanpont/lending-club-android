package com.pontlabs.lendingclub.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewUtils {

    private final int mShortAnimTime;

    @Inject ViewUtils(Context context) {
        mShortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public void fade(final boolean in, final View view) {
        view.setVisibility(in ? View.GONE : View.VISIBLE);
        view.animate().setDuration(mShortAnimTime).alpha(
                in ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(in ? View.GONE : View.VISIBLE);
            }
        });
    }
}
