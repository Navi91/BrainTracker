package com.orgazmpionerki.braintracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.orgazmpionerki.braintracker.controller.NotificationController;
import com.orgazmpionerki.braintracker.datasource.UpdateDataManager;
import com.orgazmpionerki.braintracker.receiver.WiFiReceiver.WiFiStateChangeListener;
import com.orgazmpionerki.braintracker.util.Tracer;

public class BrainTrackerService extends Service implements WiFiStateChangeListener {
    public final static String DEBUG_TAG = "brain_tracker_service_debug";

    private final static int FOREGROUND_ID = 777;

    private UpdateDataManager mUpdateDataManager;
    private NotificationController mNotificationController;

    @Override
    public void onCreate() {
        super.onCreate();
        Tracer.methodEnter(DEBUG_TAG);

        mUpdateDataManager = UpdateDataManager.getInstance(this);
        mNotificationController = NotificationController.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Tracer.methodEnter(DEBUG_TAG);

        mUpdateDataManager.start();
        mNotificationController.onServiceStarted();
//        startForeground(FOREGROUND_ID, mNotificationController.createAndroidNotification());

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Tracer.methodEnter(DEBUG_TAG);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Tracer.methodEnter(DEBUG_TAG);

        mUpdateDataManager.stop();
        mNotificationController.onServiceStopped();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Tracer.methodEnter(DEBUG_TAG);
        return null;
    }

    private void showPopup(Context context, String title, int points) {
        Intent intent = new Intent(context, PopupService.class);
        intent.putExtra(PopupService.EXTRA_VIDEO_TITLE, title);
        intent.putExtra(PopupService.EXTRA_BRAIN_POINTS_BEFORE, points);
        context.startService(intent);
    }

    @Override
    public void onWiFiStateChange() {

    }
}
