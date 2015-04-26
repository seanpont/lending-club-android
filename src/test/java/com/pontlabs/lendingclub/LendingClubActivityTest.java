package com.pontlabs.lendingclub;

import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class LendingClubActivityTest extends BaseTest {

    @Test
    public void show_login_screen_when_unauthenticated() throws Exception {
        assertThat(mActivity.mSignInView.getVisibility(), is(View.VISIBLE));

    }
}
