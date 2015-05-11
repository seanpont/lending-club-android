package com.pontlabs.lendingclub.utils;

import javax.inject.Inject;

public class MockThreadUtils extends ThreadUtils {

  @Override public void onMainThread(Runnable runnable) {
    runnable.run();
  }
}
