package com.orgazmpionerki.braintracker;

import android.app.Application;

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
        Tracer.debug("start_debug", "onCreate");
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}
