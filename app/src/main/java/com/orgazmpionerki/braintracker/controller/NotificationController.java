package com.orgazmpionerki.braintracker.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.MainActivity;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.datasource.UpdateDataManager;
import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequest;
import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequestListener;
import com.orgazmpionerki.braintracker.service.controllers.BrainTrackerServiceController;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Tracer;

public class NotificationController implements IUpdateRequestListener {
    private static NotificationController mInstance;

    private Context mContext;
    private BrainTrackerDatabase mDatabase;

    public static NotificationController getInstance(Context context) {
        if (mInstance == null) {
            return new NotificationController(context);
        }

        return mInstance;
    }

    private NotificationController(Context context) {
        mContext = context;
        mDatabase = new BrainTrackerDatabase(context);
    }

    public void onServiceStarted() {
        mDatabase.open();
        UpdateDataManager.getInstance(mContext).addListener(this);
        updateNotifications();
    }

    public void onServiceStopped() {
        mDatabase.close();
        UpdateDataManager.getInstance(mContext).removeListener(this);
        removeAndroidNotification();
    }

    public Notification createAndroidNotification() {
        return createAndroidNotification(getCurrentDayPoints());
    }

    private Notification createAndroidNotification(int points) {
        String today_points = "Today: ";

        if (points > 0) {
            today_points += "+" + points;
        } else {
            today_points += points;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.brain);
        builder.setContentTitle(getNotificationMessage());
        builder.setContentText(today_points);
        builder.setOngoing(true);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending_intent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pending_intent);

        return builder.build();
    }

    private void updateNotifications() {
        if (!new BrainTrackerServiceController().isServiceRunning(mContext)) {
            return;
        }

        updateAndroidNotification();
    }

    private void updateAndroidNotification() {
        Notification androidNotification = createAndroidNotification(getCurrentDayPoints());
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFICATION_ID, androidNotification);
    }

    private void removeAndroidNotification() {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(Constants.NOTIFICATION_ID);
    }

    private String getNotificationMessage() {
        String message = "";

        if (WifiController.isWifiConnected(mContext)) {
            message = mContext.getResources().getString(R.string.notification_service_run);
        } else {
            message = mContext.getResources().getString(R.string.notification_service_wifi_off);
        }

        return message;
    }

    private int getCurrentDayPoints() {
        if (mDatabase != null) {
            final int requestDays = 1;
            int points = mDatabase.getBrainPoints(requestDays);
            return points;
        }

        return 0;
    }

    @Override
    public void onUpdateDone(IUpdateRequest request) {
        updateNotifications();
    }

//    public void addListener(OnChangePointsListener listener) {
//        if (mListeners != null) {
//            mListeners.add(listener);
//        } else {
//            mListeners = new ArrayList<OnChangePointsListener>();
//            mListeners.add(listener);
//        }
//    }
//
//    public void removeListener(OnChangePointsListener listener) {
//        mListeners.remove(listener);
//
//        if (mListeners.size() == 0) {
//            mListeners = null;
//        }
//    }
//
//    private void notifyAllListeners() {
//        if (mListeners == null) {
//            return;
//        }
//
//        for (OnChangePointsListener listener : mListeners) {
//            if (listener != null) {
//                listener.onChangePoints();
//            }
//        }
//    }


}