package com.pontlabs.lendingclub;

import android.app.Application;

import dagger.ObjectGraph;

public class LendingClubApplication extends Application {

    private ObjectGraph mGraph;

    @Override public void onCreate() {
        super.onCreate();
        mGraph = ObjectGraph.create(new LendingClubModule());
    }

    public void inject(Object object) {
        mGraph.inject(object);
    }
}
