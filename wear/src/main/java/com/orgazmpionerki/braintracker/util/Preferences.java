package com.orgazmpionerki.braintracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class Preferences {
    private static Preferences mInstance;


    public static Preferences getSharedPreferences() {
        if (mInstance == null) {
            mInstance = new Preferences();
        }

        return mInstance;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("com.braintracker.app", Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
    }

}
