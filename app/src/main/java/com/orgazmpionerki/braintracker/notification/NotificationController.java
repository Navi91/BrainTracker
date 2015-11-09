package com.orgazmpionerki.braintracker.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.activity.MainActivity;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.datasource.UpdateDataManager;
import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequest;
import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequestListener;
import com.orgazmpionerki.braintracker.datasource.updaterequest.UpdateRequest;
import com.orgazmpionerki.braintracker.service.PopupService;
import com.orgazmpionerki.braintracker.service.controllers.BrainTrackerServiceController;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;

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
        updateNotifications(null);
    }

    public void onServiceStopped() {
        mDatabase.close();
        UpdateDataManager.getInstance(mContext).removeListener(this);
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

    private void updateNotifications(IUpdateRequest request) {
        updateAndroidNotification();
        updatePopupNotification(request);
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

    private void updatePopupNotification(IUpdateRequest request) {
        if (!Preferences.getShowPopupNotification(mContext) || request == null) {
            return;
        }

        Bundle requestInfo = request.getInfo();

        if (requestInfo != null && requestInfo.containsKey(UpdateRequest.BUNDLE_BEFORE_POINTS)) {
            Intent popupIntent = createPopupIntent(requestInfo.getInt(UpdateRequest.BUNDLE_BEFORE_POINTS), getCurrentDayPoints());
            mContext.startService(popupIntent);
        }
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
        if (!new BrainTrackerServiceController().isServiceRunning(mContext)) {
            return;
        }

        updateNotifications(request);
    }
}