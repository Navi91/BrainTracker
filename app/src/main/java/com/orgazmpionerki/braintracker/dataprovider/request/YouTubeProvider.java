package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;

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

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class YouTubeProvider {
    private static YouTube sYouTube;

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
}
