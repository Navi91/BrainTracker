package com.orgazmpionerki.braintracker.util;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static final String DEFAULT_CATEGORY = "{\"1\": 1, \"2\": 1, \"3\": -1, \"4\": -1, \"5\": -1, \"6\": -1, \"7\": -1, \"8\": -1, \"9\": -1, \"10\": -1, \"11\": -2, \"12\": -1, \"13\": 1, \"14\": 1, \"15\": -2, \"16\": -1, \"17\": -1, \"18\": 1, \"19\": 1, \"20\": -2, \"21\": -2, \"22\": -1, \"23\": -2, \"24\": -2, \"25\": 1, \"26\": 2, \"27\": 2, \"28\": 2, \"29\": -1, \"30\": -1, \"31\": -2, \"32\": -1, \"33\": 1, \"34\": -1, \"35\": 1, \"36\": -1, \"37\": 1, \"38\": -1, \"39\": -1, \"40\": -1, \"41\": -1, \"42\": 1, \"43\": -1, \"44\": 1, \"45\": -1 }";

    private static SharedPreferences getShared(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getShared(context).edit();
    }

    private static void putString(Context context, String prefKey, String value) {
        getEditor(context).putString(prefKey, value).commit();
    }

    // ------------------------ Category ---------------------------------
    public static String getCategory(Context context) {
        return getShared(context).getString("category", DEFAULT_CATEGORY);
    }

    // ------------------------ Access Key ---------------------------------
    public static String getAccessKey(Context context) {
        return getShared(context).getString("access_key", null);
    }

    public static void putAccessKey(Context context, String key) {
        getShared(context).edit().putString("access_key", key).commit();
    }

    // ------------------------ Refresh Key ---------------------------------
    public static String getRefreshKey(Context context) {
        return getShared(context).getString("refresh_key", null);
    }

    public static void putRefreshKey(Context context, String key) {
        getShared(context).edit().putString("refresh_key", key).commit();
    }

    // ------------------------ Auth Code ---------------------------
    public static String getAuthCode(Context context) {
        return getShared(context).getString("auth_code", null);
    }

    public static void putAuthCode(Context context, String code) {
        getShared(context).edit().putString("auth_code", code).commit();
    }

    // ------------------------ Last Update Time -------------------------------
    public static long getLastUpdateTime(Context context) {
        return getShared(context).getLong("last_update_time", Calendar.getInstance().getTimeInMillis());
    }

    public static void putLastUpdateTime(Context context, long time) {
        getShared(context).edit().putLong("last_update_time", time).commit();
    }

    // ------------------------- Statistics Period -------------------------------
    public static int getStatisticsPeriod(Context context) {
        return getShared(context).getInt("statistics_period", 0);
    }

    public static void putStatisticsPeriod(Context context, int period) {
        getShared(context).edit().putInt("statistics_period", period).commit();
    }

    // ------------------------ Begin Date ---------------------------------
    public static long getBeginDate(Context context) {
        return getShared(context).getLong("begin_date", 0L);
    }

    public static void putBeginDate(Context context, long time) {
        getShared(context).edit().putLong("begin_date", time).commit();
    }

    // ------------------------ Default Target Value ---------------------------------
    public static int getTargetValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("setting_statistics_target_key", 10);
    }

    // ------------------------ Show Popup Notification Setting ---------------------------------
    public static boolean getShowPopupNotification(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("setting_android_notification_key", false);
    }

    private static final String TOKEN_PREF_KEY = "token_pref_key";

    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(TOKEN_PREF_KEY, "");
    }

    public static void setToken(Context context, String token) {
        getEditor(context).putString(TOKEN_PREF_KEY, token).commit();
    }

    private static final String ACCOUNT_EMAIL_PREF_KEY = "account_email_pref_key";

    public static String getAccountEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(ACCOUNT_EMAIL_PREF_KEY, "");
    }

    public static void setAccountEmail(Context context, String email) {
        putString(context, ACCOUNT_EMAIL_PREF_KEY, email);
    }
}
