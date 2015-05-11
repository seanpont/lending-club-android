package com.pontlabs.lendingclub;

import android.view.View;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Matchers {

  public static Matcher<? super View> isVisible() {
    return new BaseMatcher<View>() {
      @Override public boolean matches(Object o) {
        return ((View) o).getVisibility() == View.VISIBLE;
      }

      @Override public void describeTo(Description description) {
        description.appendText("visible");
      }

      @Override public void describeMismatch(Object item, Description description) {
        description.appendText("gone");
      }
    };
  }

  public static Matcher<? super View> isGone() {
    return new BaseMatcher<View>() {
      @Override public boolean matches(Object o) {
        return ((View) o).getVisibility() == View.GONE;
      }

      @Override public void describeTo(Description description) {
        description.appendText("gone");
      }

      @Override public void describeMismatch(Object item, Description description) {
        description.appendText("visible");
      }
    };
  }


}
