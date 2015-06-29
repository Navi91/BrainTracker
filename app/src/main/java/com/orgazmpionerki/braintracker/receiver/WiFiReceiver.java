package com.orgazmpionerki.braintracker.receiver;

import java.util.ArrayList;
import java.util.List;

import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Tracer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class WiFiReceiver extends BroadcastReceiver {
	private static List<WiFiStateChangeListener> mListeners;
	private static WiFiReceiver mInstance;

	public interface WiFiStateChangeListener {
		public void onWiFiStateChange();
	}

	public static WiFiReceiver newInstance() {
		return new WiFiReceiver();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Tracer.debug("WiFI state change");
		notifyListeners();
	}

	private void notifyListeners() {
		for (WiFiStateChangeListener listener : mListeners) {
			listener.onWiFiStateChange();
		}
	}

	public static void addListener(Context context, WiFiStateChangeListener listener) {
		if (mListeners != null) {
			mListeners.add(listener);
		} else {
			mInstance = newInstance();
			context.registerReceiver(mInstance, new IntentFilter(Constants.WIFI_INTENT_FILTER_ACTION));

			mListeners = new ArrayList<WiFiStateChangeListener>();
			mListeners.add(listener);
		}
	}

	public static void removeListener(Context context, WiFiStateChangeListener listener) {
		mListeners.remove(listener);

		if (mListeners.size() == 0) {
			mListeners = null;
			context.unregisterReceiver(mInstance);
			mInstance = null;
		}
	}
}
