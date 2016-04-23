package com.orgazmpionerki.braintracker;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.dkrasnov.util_android_lib.Tracer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Dmitriy on 03.11.2015.
 */
public class App extends Application {
    public static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Tracer.setDefaultTag("brain_tracker_trace");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
