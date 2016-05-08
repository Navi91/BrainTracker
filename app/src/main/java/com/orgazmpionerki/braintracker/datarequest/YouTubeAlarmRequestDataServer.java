package com.orgazmpionerki.braintracker.datarequest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.service.TaskReceiver;
import com.orgazmpionerki.braintracker.util.Preferences;

/**
 * Created by Dmitriy on 08.05.2016.
 */
public class YouTubeAlarmRequestDataServer implements RequestDataServer {
    private static YouTubeAlarmRequestDataServer instance;

    private Context context;

    public static RequestDataServer getInstance(Context context) {
        if (instance == null) {
            instance = new YouTubeAlarmRequestDataServer(context);
        }

        return instance;
    }

    private YouTubeAlarmRequestDataServer(Context context) {
        this.context = context;
    }

    @Override
    public void start() {
        Tracer.methodEnter();
        if (running()) {
            return;
        }

        Preferences.setServerRunning(context, true);

        final PendingIntent pendingIntent = createPendingIntent();

        long firstMillis = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, firstMillis, 20 * 1000L, pendingIntent);
    }

    @Override
    public void stop() {
        Tracer.methodEnter();
        if (!running()) {
            return;
        }

        Preferences.setServerRunning(context, false);

        final PendingIntent pendingIntent = createPendingIntent();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public boolean running() {
        return Preferences.getServerRunning(context);
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(context, TaskReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, TaskReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
