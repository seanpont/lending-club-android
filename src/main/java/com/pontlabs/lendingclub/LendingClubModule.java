package com.pontlabs.lendingclub;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

/**
 * Dependency Injection
 */
@Module(
        injects = LendingClubActivity.class,
        complete = false
)
public class LendingClubModule {

    @Provides @Singleton OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides @Singleton EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
