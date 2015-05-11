package com.pontlabs.lendingclub;

import android.os.Message;

/**
 * Client is not returning calls because handler not working in Robolectric land.
 * This should make everything run now on the main thread.
 */
public class MockHandler extends android.os.Handler {

  @Override public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
    msg.getCallback().run();
    return true;
  }



}
