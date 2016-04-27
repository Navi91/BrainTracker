package com.orgazmpionerki.braintracker.datarequest.request;

import android.content.Context;

import com.crashlytics.android.core.CrashlyticsCore;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class YouTubeProvider {
    private static YouTube sYouTube;
    private static Map<String, Integer> mCategoriesValue;

    static {
        initCategories();
    }

    public static YouTube getInstance(Context context) {
        if (sYouTube == null) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    context, Arrays.asList(YouTubeScopes.YOUTUBE))
                    .setBackOff(new ExponentialBackOff())
                    .setSelectedAccountName(Preferences.getAccountEmail(context));

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            sYouTube = new YouTube.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Google API Android Quickstart")
                    .build();
        }

        return sYouTube;
    }

    public static int getCategoryValue(String category) {
        if (mCategoriesValue.containsKey(category)) {
            return mCategoriesValue.get(category);
        } else {
            CrashlyticsCore.getInstance().logException(new IllegalArgumentException("This category not found " + category));
            return 0;
        }
    }

    private static void initCategories() {
        mCategoriesValue = new HashMap<>();

        mCategoriesValue.put("1", 1);
        mCategoriesValue.put("2", 1);
        mCategoriesValue.put("3", -1);
        mCategoriesValue.put("4", -1);
        mCategoriesValue.put("5", -1);
        mCategoriesValue.put("6", -1);
        mCategoriesValue.put("7", -1);
        mCategoriesValue.put("8", -1);
        mCategoriesValue.put("9", -1);
        mCategoriesValue.put("10", -1);
        mCategoriesValue.put("11", -2);
        mCategoriesValue.put("12", -1);
        mCategoriesValue.put("13", 1);
        mCategoriesValue.put("14", 1);
        mCategoriesValue.put("15", -2);
        mCategoriesValue.put("16", -1);
        mCategoriesValue.put("17", -1);
        mCategoriesValue.put("18", 1);
        mCategoriesValue.put("19", 1);
        mCategoriesValue.put("20", -2);
        mCategoriesValue.put("21", -2);
        mCategoriesValue.put("22", -1);
        mCategoriesValue.put("23", -2);
        mCategoriesValue.put("24", -2);
        mCategoriesValue.put("25", 1);
        mCategoriesValue.put("26", 2);
        mCategoriesValue.put("27", 2);
        mCategoriesValue.put("28", 2);
        mCategoriesValue.put("29", -1);
        mCategoriesValue.put("30", -1);
        mCategoriesValue.put("31", -2);
        mCategoriesValue.put("32", -1);
        mCategoriesValue.put("33", 1);
        mCategoriesValue.put("34", -1);
        mCategoriesValue.put("35", 1);
        mCategoriesValue.put("36", -1);
        mCategoriesValue.put("37", 1);
        mCategoriesValue.put("38", -1);
        mCategoriesValue.put("39", -1);
        mCategoriesValue.put("40", -1);
        mCategoriesValue.put("41", -1);
        mCategoriesValue.put("42", 1);
        mCategoriesValue.put("43", -1);
        mCategoriesValue.put("44", 1);
        mCategoriesValue.put("45", -1);
    }

}
