package com.pontlabs.lendingclub;

import com.pontlabs.lendingclub.api.Credentials;

import org.junit.Test;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class LendingClubDataTest extends BaseTest {

    @Test
    public void testCredentials() {
        assertThat(mData.getCredentials(), nullValue());
        Credentials credentials = Credentials.builder().accountId(124941).apiKey("apiKey").build();
        mData.saveCredentials(credentials);
        assertThat(mData.getCredentials(), is(credentials));
    }
}
