package com.pontlabs.lendingclub.utils;

import java.lang.ref.WeakReference;

public class ObjectUtils {

  public static <T> WeakReference<T> weak(T val) {
    return new WeakReference<>(val);
  }

  public static <T> T nullToDefault(T val, T defaultVal) {
    return val == null ? defaultVal : val;
  }
}
