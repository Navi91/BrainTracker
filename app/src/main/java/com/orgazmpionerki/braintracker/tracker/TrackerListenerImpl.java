package com.orgazmpionerki.braintracker.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dkrasnov.util_android_lib.ResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 17.05.2016.
 */
public class TrackerListenerImpl implements TrackerListener {
    public static final String TRACK_EVENT_ACTION = "com.orgazmpionerki.braintracker.tracker.track_event_action";
    public static final String TRACK_EVENT_EXTRA = "com.orgazmpionerki.braintracker.tracker.track_event_extra";

    private Context context;
    private BroadcastReceiver receiver;
    private List<ResultListener<TrackEvent>> listeners;

    public class TrackEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO
            TrackEvent trackEvent = (TrackEvent) intent.getSerializableExtra(TRACK_EVENT_EXTRA);
            notifyListeners(trackEvent);
        }
    }

    public TrackerListenerImpl(Context context) {
        this.context = context;
        receiver = new TrackEventReceiver();
        listeners = new ArrayList<>();
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(TRACK_EVENT_ACTION);
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        listeners.clear();
        context.unregisterReceiver(receiver);
    }

    @Override
    public void addListener(ResultListener<TrackEvent> listener) {
        listeners.add(listener);
    }

    private void notifyListeners(TrackEvent trackEvent) {
        for (ResultListener<TrackEvent> listener: listeners) {
            listener.onResult(trackEvent);
        }
    }
}
