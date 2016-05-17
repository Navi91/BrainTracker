package com.orgazmpionerki.braintracker.tracker;

import com.dkrasnov.util_android_lib.ResultListener;

/**
 * Created by Dmitriy on 17.05.2016.
 */
public interface TrackerListener {
    void onStart();

    void onStop();

    void addListener(ResultListener<TrackEvent> listener);
}
