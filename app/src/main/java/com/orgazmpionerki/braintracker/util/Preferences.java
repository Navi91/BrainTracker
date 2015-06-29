package com.orgazmpionerki.braintracker.util;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
	private static Preferences mInstance;

	private static final String DEFAULT_CATEGORY = "{\"1\": 1, \"2\": 1, \"3\": -1, \"4\": -1, \"5\": -1, \"6\": -1, \"7\": -1, \"8\": -1, \"9\": -1, \"10\": -1, \"11\": -2, \"12\": -1, \"13\": 1, \"14\": 1, \"15\": -2, \"16\": -1, \"17\": -1, \"18\": 1, \"19\": 1, \"20\": -2, \"21\": -2, \"22\": -1, \"23\": -2, \"24\": -2, \"25\": 1, \"26\": 2, \"27\": 2, \"28\": 2, \"29\": -1, \"30\": -1, \"31\": -2, \"32\": -1, \"33\": 1, \"34\": -1, \"35\": 1, \"36\": -1, \"37\": 1, \"38\": -1, \"39\": -1, \"40\": -1, \"41\": -1, \"42\": 1, \"43\": -1, \"44\": 1, \"45\": -1 }";

	public static Preferences getSharedPrefernces() {
		if (mInstance == null) {
			mInstance = new Preferences();
		}

		return mInstance;
	}

	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences("com.braintracker.app", Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
	}

	// ------------------------ Category ---------------------------------
	public static String getCategory(Context context) {
		return getSharedPreferences(context).getString("category", DEFAULT_CATEGORY);
	}

	public static void putCategory(Context context, String s) {
		getSharedPreferences(context).edit().putString("category", s).commit();
	}

	// ------------------------ Key time ---------------------------------
	public static long getKeyTime(Context context) {
		return getSharedPreferences(context).getLong("key_time", 0L);
	}

	public static void putKeyTime(Context context, long time) {
		getSharedPreferences(context).edit().putLong("key_time", time).commit();
	}

	// ------------------------ Access Key ---------------------------------
	public static String getAccessKey(Context context) {
		return getSharedPreferences(context).getString("access_key", null);
	}

	public static void putAccessKey(Context context, String key) {
		getSharedPreferences(context).edit().putString("access_key", key).commit();
	}

	// ------------------------ Refresh Key ---------------------------------
	public static String getRefreshKey(Context context) {
		return getSharedPreferences(context).getString("refresh_key", null);
	}

	public static void putRefreshKey(Context context, String key) {
		getSharedPreferences(context).edit().putString("refresh_key", key).commit();
	}

	// ------------------------ Auth Code ---------------------------
	public static String getAuthCode(Context context) {
		return getSharedPreferences(context).getString("auth_code", null);
	}

	public static void putAuthCode(Context context, String code) {
		getSharedPreferences(context).edit().putString("auth_code", code).commit();
	}

	// ------------------------ Last Update Time -------------------------------
	public static long getLastUpdateTime(Context context) {
		return getSharedPreferences(context).getLong("last_update_time", Calendar.getInstance().getTimeInMillis());
	}

	public static void putLastUpdateTime(Context context, long time) {
		getSharedPreferences(context).edit().putLong("last_update_time", time).commit();
	}

	// ------------------------- Statistics Period -------------------------------
	public static int getStatisticsPeriod(Context context) {
		return getSharedPreferences(context).getInt("statistics_period", 0);
	}

	public static void putStatisticsPeriod(Context context, int period) {
		getSharedPreferences(context).edit().putInt("statistics_period", period).commit();
	}

	// ------------------------ Begin Date ---------------------------------
	public static long getBeginDate(Context context) {
		return getSharedPreferences(context).getLong("begin_date", 0L);
	}

	public static void putBeginDate(Context context, long time) {
		getSharedPreferences(context).edit().putLong("begin_date", time).commit();
	}

	// ------------------------ Last Start ---------------------------------
	public static long getLastServiceStart(Context context) {
		return getSharedPreferences(context).getLong("last_start", 0L);
	}

	public static void putLastServiceStart(Context context, long time) {
		getSharedPreferences(context).edit().putLong("last_start", time).commit();
	}

	// -------------------------- DEFAULT SETTINGS -----------------------------------

	// ------------------------ Default Target Value ---------------------------------
	public static int getTargetValue(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt("setting_statistics_target_key", 10);
	}
}
