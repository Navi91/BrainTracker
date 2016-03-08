package com.orgazmpionerki.braintracker.outh2;

import android.content.Context;
import android.text.TextUtils;

import com.orgazmpionerki.braintracker.util.Preferences;

/**
 * Created by Dmitriy on 08.03.2016.
 */
public class GoogleAuthToken {

    public static boolean exist(Context context) {
        return !TextUtils.isEmpty(Preferences.getToken(context));
    }

    public static void setToken(Context context, String token) {
        Preferences.setToken(context, token);
    }

    public static String getToken(Context context) {
        return Preferences.getToken(context);
    }

    public static void clear(Context context) {
        Preferences.setToken(context, "");
    }
}