package com.braintracker.controller;

import java.util.ArrayList;
import java.util.List;

import com.braintracker.MainActivity;
import com.braintracker.R;
import com.braintracker.util.Constants;
import com.braintracker.util.Preferences;
import com.braintracker.util.Tracer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class BrainTrackerNotificationController {
	private static List<OnChangePointsListener> mListeners;

	public static void createNotifications(Context context, int points) {
		Tracer.debug("CREATE CONTROL NOTIFICATIONS");
		createAndroidNotification(context, points);
		notifyAllListeners();
	}

	public static void createAndroidNotification(Context context, int points) {
		String today_points = "Today: ";

		if (points > 0) {
			today_points += "+" + points;
		} else {
			today_points += points;
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.drawable.brain);
		builder.setContentTitle(getNotificationMessage(context));
		builder.setContentText(today_points);
		builder.setOngoing(true);

		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pending_intent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(pending_intent);
		// Gets an instance of the NotificationManager service
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		manager.notify(Constants.NOTIFICATION_ID, builder.build());
	}

	public static void updateNotifications(Context context, int points) {
		if (!Preferences.getIsServiceRunning(context)) {
			return;
		}

		createNotifications(context, points);
	}

	public static void removeAndroidNotification(Context context) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(Constants.NOTIFICATION_ID);
	}

	private static String getNotificationMessage(Context context) {
		String message = "";

		if (WifiController.isWifiConnected(context)) {
			message = context.getResources().getString(R.string.notification_service_run);
		} else {
			message = context.getResources().getString(R.string.notification_service_wifi_off);
		}

		return message;
	}

	public static void addListener(OnChangePointsListener listener) {
		Tracer.debug("ADD LISTENER");
		if (mListeners != null) {
			mListeners.add(listener);
		} else {
			mListeners = new ArrayList<OnChangePointsListener>();
			mListeners.add(listener);
		}
	}

	public static void removeListener(OnChangePointsListener listener) {
		Tracer.debug("REMOVE LISTENER");
		mListeners.remove(listener);

		if (mListeners.size() == 0) {
			mListeners = null;
		}
	}

	private static void notifyAllListeners() {
		Tracer.debug("NOTIFY ALL LISTENERS");
		if (mListeners == null) {
			return;
		}
		Tracer.debug("LISTENERS NOT NULL");

		for (OnChangePointsListener listener : mListeners) {
			if (listener != null) {
				Tracer.debug("NOTIFY LISTENER");
				listener.onChangePoints();
			}
		}
		checkListeners();
	}

	public static void checkListeners() {
		if (mListeners != null) {
			Tracer.debug("CHECK LISTENERS NOT NULL!!!!!!!!!");
		} else {
			Tracer.debug("CHECK LISTENERS NULL!!!!!!!!!");
		}
	}
}