package com.braintracker.service;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.braintracker.auth.tokens.TokenRetrievedListener;
import com.braintracker.auth.tokens.Tokens;
import com.braintracker.auth.tokens.TokensTask;
import com.braintracker.controller.BrainTrackerNotificationController;
import com.braintracker.controller.OnChangePointsListener;
import com.braintracker.controller.WifiController;
import com.braintracker.database.BrainTrackerDatabase;
import com.braintracker.receiver.WiFiReceiver;
import com.braintracker.receiver.WiFiReceiver.WiFiStateChangeListener;
import com.braintracker.util.Constants;
import com.braintracker.util.Preferences;
import com.braintracker.util.StreamConverter;
import com.braintracker.util.MetricsConverter;
import com.braintracker.util.Tracer;

public class BrainTrackerService extends BroadcastReceiver implements TokenRetrievedListener, WiFiStateChangeListener {
	private BrainTrackerDatabase mDatabase;
	private Context mContext;

	@SuppressLint("Wakelock")
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();

		if (mContext == null) {
			mContext = context;
		}

		if (mDatabase == null) {
			mDatabase = new BrainTrackerDatabase(context);
			mDatabase.open();
		}

		checkUpdates(context);

		wl.release();
	}

	public void startService(Context context) {
		mContext = context;

		AlarmManager alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, BrainTrackerService.class);
		PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.UPDATE_TIME, pending_intent);

		mDatabase = new BrainTrackerDatabase(context);
		mDatabase.open();

		Preferences.putIsServiceRunning(context, true);
		Preferences.putLastServiceStart(context, Calendar.getInstance().getTimeInMillis());

		registerOnChangePointsListener((OnChangePointsListener) context);
		registerWifiReceiver();
		createNotification(context);
	}

	public void stopService(Context context) {
		Intent intent = new Intent(context, BrainTrackerService.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarm_manager.cancel(sender);

		if (mDatabase != null) {
			mDatabase.close();
		}

		Preferences.putIsServiceRunning(context, false);

		unregisterOnChangePointsListener((OnChangePointsListener) context);
		unregisterWifiReceiver();
		removeNotification(context);
	}

	private void checkUpdates(Context context) {
		BrainTrackerNotificationController.checkListeners();
		Tracer.debug("UPDATE");

		if (!WifiController.isWifiConnected(context)) {
			Tracer.debug("WIFI NOT WORKED");
			return;
		}

		new WatchHistoryTask(context).execute(Preferences.getAccessKey(context));
	}

	private class WatchHistoryTask extends AsyncTask<String, Void, String> {
		Context mContext;

		public WatchHistoryTask(Context context) {
			mContext = context;
		}

		@Override
		protected String doInBackground(String... urls) {
			String access_key = urls[0];
			try {
				HttpResponse response = requestPlayListId(access_key);

				JSONObject jsonObject = StreamConverter.convertHttpResponseToJsonObject(response);

				// detect expiring of access key
				if (jsonObject.has("error") && jsonObject.getJSONObject("error").toString().contains("Invalid Credentials")) {
					refreshAccessKey(mContext);
					return "";
				}

				String historyPlaylistId = jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("contentDetails").getJSONObject("relatedPlaylists").getString("watchHistory");

				HttpResponse playlistResponse = requestPlaylist(historyPlaylistId, access_key);
				JSONObject playlistObject = StreamConverter.convertHttpResponseToJsonObject(playlistResponse);
				JSONArray videosInPlaylist = playlistObject.getJSONArray("items");
				int results = (int) Math.min(playlistObject.getJSONObject("pageInfo").getLong("resultsPerPage"), playlistObject.getJSONObject("pageInfo").getLong("totalResults"));

				for (int i = 0; i < results; i++) {

					JSONObject video = videosInPlaylist.getJSONObject(i);
					String video_id = video.getJSONObject("contentDetails").getString("videoId");
					HttpResponse videoInfo = requestVideoInfo(video_id, access_key);
					JSONObject videoObject = StreamConverter.convertStreamToJsonObject(videoInfo.getEntity().getContent());

					if (videoObject.getJSONObject("pageInfo").getInt("totalResults") == 0) {
						continue;
					}

					JSONObject items = videoObject.getJSONArray("items").getJSONObject(0);

					String id = items.getString("id");
					String category = items.getJSONObject("snippet").getString("categoryId");
					String title = items.getJSONObject("snippet").getString("title");
					String length;
					if (Calendar.getInstance().getTimeInMillis() - Preferences.getLastServiceStart(mContext) > 10 * 1000) {
						length = items.getJSONObject("contentDetails").getString("duration");
					} else {
						length = "PT1S";
					}

					int brain_points = MetricsConverter.getBrainPoints(mContext, category, MetricsConverter.getVideoLength(length));

					if (addVideoToHistiry(id, title, category, length, brain_points, Calendar.getInstance().getTimeInMillis())) {
						// Tracer.debug("VIDEO ADDED TO DB");

						showPopup(mContext, title, brain_points);
						updateNotification();

						// TODO send data to samsung watch
					}
				}

				Preferences.putLastUpdateTime(mContext, Calendar.getInstance().getTimeInMillis());

				return null;
			} catch (IllegalStateException e) {
				Tracer.error("ERROR BT", e);
			} catch (JSONException e) {
				Tracer.error("ERROR BT", e);
			} catch (IOException e) {
				Tracer.error("ERROR BT", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {}
	}

	private HttpResponse requestPlayListId(String oauth2) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(Constants.BASE_REUEST_URL + "/channels?mine=true&part=contentDetails&access_token=" + oauth2);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		return response;
	}

	private HttpResponse requestPlaylist(String playlistId, String oauth2) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(Constants.BASE_REUEST_URL + "/playlistItems?part=contentDetails&maxResults=5&playlistId=" + playlistId + "&access_token=" + oauth2);
		// Tracer.debug("GET " + get.toString());
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		return response;
	}

	private HttpResponse requestVideoInfo(String videoId, String oauth2) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(Constants.BASE_REUEST_URL + "/videos?id=" + videoId + "&part=contentDetails,snippet&access_token=" + oauth2);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		return response;
	}

	private boolean addVideoToHistiry(String video_id, String title, String category, String length, int points, long time) {
		// Tracer.debug("ADD VIDEO " + video_id + " " + title + " " + category + " " + length + " " + time);
		return mDatabase.addVideoToWatchHistory(video_id, title, category, MetricsConverter.getVideoLength(length), points, time);
	}

	private void refreshAccessKey(Context context) {
		new Thread(new TokensTask(context, Preferences.getAuthCode(context), this)).start();
	}

	private void showPopup(Context context, String title, int points) {
		Intent intent = new Intent(context, PopupService.class);
		intent.putExtra(PopupService.EXTRA_VIDEO_TITLE, title);
		intent.putExtra(PopupService.EXTRA_BRAIN_POINTS, points);
		context.startService(intent);
	}

	private void createNotification(Context context) {
		Tracer.debug("CREATE NOTIFICATION");
		BrainTrackerNotificationController.createNotifications(context, mDatabase.getBrainPoints(1));
	}

	private void removeNotification(Context context) {
		Tracer.debug("REMOVE NOTIFICATION");
		BrainTrackerNotificationController.removeAndroidNotification(context);
	}

	private void updateNotification() {
		Tracer.debug("UPDATE NOTIFICATION");
		BrainTrackerNotificationController.updateNotifications(mContext, mDatabase.getBrainPoints(1));
	}

	private void registerWifiReceiver() {
		WiFiReceiver.addListener(mContext, this);
	}

	private void unregisterWifiReceiver() {
		WiFiReceiver.removeListener(mContext, this);
	}

	private void registerOnChangePointsListener(OnChangePointsListener listener) {
		// BrainTrackerNotificationController.addListener(listener);
	}

	private void unregisterOnChangePointsListener(OnChangePointsListener listener) {
		// BrainTrackerNotificationController.removeListener(listener);
	}

	@Override
	public void onTokensRetrieved(Tokens tokens) {}

	@Override
	public void onWiFiStateChange() {
		updateNotification();
	}
}
