package com.pontlabs.lendingclub;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontlabs.lendingclub.api.LendingClubClient;
import com.pontlabs.lendingclub.utils.NetworkStateManager;
import com.pontlabs.lendingclub.utils.ViewUtils;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class LendingClubApplication extends Application {

    public static LendingClubComponents Components;

    @Override public void onCreate() {
        super.onCreate();
        configureStrictMode();
        configureLogger();
        configureDI();
    }

    private void configureStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDeathOnNetwork().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().detectActivityLeaks().penaltyLog().build());
    }

    // ===== LOGGING ===============================================================================

    private void configureLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {

        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            Log.println(priority, tag, message);
        }

    }
    // ===== DEPENDENCY INJECTION ==================================================================

    private void configureDI() {
        Components = DaggerLendingClubApplication_LendingClubComponents.builder()
                .systemServicesModule(new SystemServicesModule(this))
                .build();

    }

    @Singleton @Component(modules = {SystemServicesModule.class, LendingClubModule.class})
    public interface LendingClubComponents {
        LendingClubClient lendingClubClient();
        LendingClubData lendingClubData();
        ViewUtils viewUtils();

        LendingClubActivity inject(LendingClubActivity activity);
    }

    @Module @SuppressWarnings("UnusedDeclaration")
    static class SystemServicesModule {

        private final Application application;

        public SystemServicesModule(Application application) {
            this.application = application;
        }

        @Provides Context provideContext(){
            return application;
        }

        @Provides @Singleton SharedPreferences providePreferenceManager() {
            return PreferenceManager.getDefaultSharedPreferences(application);
        }

        @Provides @Singleton ConnectivityManager provideConnectivityManager() {
            return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        @Provides @Singleton NetworkStateManager provideNetworkStateManager(ConnectivityManager connectivityManagerCompat) {
            return new NetworkStateManager(connectivityManagerCompat);
        }
    }

    @Module @SuppressWarnings("UnusedDeclaration")
    static class LendingClubModule {

        @Provides @Singleton OkHttpClient provideOkHttpClient() {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
            return okHttpClient;
        }

        @Provides @Singleton EventBus provideEventBus() {
            return EventBus.getDefault();
        }

        @Provides @Singleton Gson provideGson() {
            return new GsonBuilder().create();
        }
    }


}
