package com.orgazmpionerki.braintracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.datarequest.RequestDataServer;
import com.orgazmpionerki.braintracker.datarequest.YouTubeRequestDataServer;
import com.orgazmpionerki.braintracker.notification.NotificationController;
import com.orgazmpionerki.braintracker.receiver.WiFiReceiver.WiFiStateChangeListener;

public class BrainTrackerService extends Service implements WiFiStateChangeListener {
    public final static String DEBUG_TAG = "brain_tracker_service_debug";

//    private NotificationController notificationController;
    private RequestDataServer server;

    @Override
    public void onCreate() {
        super.onCreate();
        Tracer.methodEnter(DEBUG_TAG);

//        notificationController = NotificationController.getInstance(this);
        server = new YouTubeRequestDataServer(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Tracer.methodEnter(DEBUG_TAG);

//        notificationController.onServiceStarted();
        server.start();

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

//        notificationController.onServiceStopped();
        server.stop();

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
