package com.orgazmpionerki.braintracker.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.activity.MainActivity;
import com.orgazmpionerki.braintracker.dataprovider.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.service.PopupService;
import com.orgazmpionerki.braintracker.util.Constants;

public class NotificationController {
    private static NotificationController mInstance;

    private Context mContext;
    private BrainTrackerDatabaseImpl mDatabase;

    public static NotificationController getInstance(Context context) {
        if (mInstance == null) {
            return new NotificationController(context);
        }

        return mInstance;
    }

    private NotificationController(Context context) {
        mContext = context;
        mDatabase = new BrainTrackerDatabaseImpl(context);
    }

    public void onServiceStarted() {
        mDatabase.open();
        updateNotifications();
    }

    public void onServiceStopped() {
        mDatabase.close();
        removeAndroidNotification();
    }

    private Notification createAndroidNotification(int points) {
        String today_points = "Brain points: ";

        if (points > 0) {
            today_points += "+" + points;
        } else {
            today_points += points;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.brain);
        builder.setContentTitle(getNotificationMessage());
        builder.setContentText(today_points);
//        builder.setOngoing(true);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending_intent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pending_intent);

        return builder.build();
    }


    private void updateAndroidNotification() {
        Notification androidNotification = createAndroidNotification(getCurrentDayPoints());
        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        manager.notify(Constants.NOTIFICATION_ID, androidNotification);
    }

    private void removeAndroidNotification() {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(Constants.NOTIFICATION_ID);
    }

    private Intent createPopupIntent(int beforePoints, int afterPoints) {
        Intent intent = new Intent(mContext, PopupService.class);
        intent.putExtra(PopupService.EXTRA_BRAIN_POINTS_BEFORE, beforePoints);
        intent.putExtra(PopupService.EXTRA_BRAIN_POINTS_AFTER, afterPoints);

        return intent;
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

    private void updateNotifications() {

    }

    private int getCurrentDayPoints() {
        return 0;
    }
}