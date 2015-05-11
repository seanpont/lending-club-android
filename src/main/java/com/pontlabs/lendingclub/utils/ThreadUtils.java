package com.pontlabs.lendingclub.utils;

import android.os.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A wrapper class around a handler because Robolectric I can't get Robolectric to
 * run the runnable on the main test thread in test land.
 */
@Singleton
public class ThreadUtils {

  @Inject Handler mHandler;
  @Inject ThreadUtils() {}

  public void onMainThread(Runnable runnable) {
    mHandler.post(runnable);
  }
}
