package com.pontlabs.lendingclub;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class LendingClubActivityTest {

    private LendingClubActivity mActivity;

    @Before
    public void setup() {
        mActivity = Robolectric.setupActivity(LendingClubActivity.class);
    }

    @Test
    public void show_login_screen_when_unauthenticated() throws Exception {
        assertThat(mActivity.mSignInView.getVisibility(), is(View.VISIBLE));

    }
}
