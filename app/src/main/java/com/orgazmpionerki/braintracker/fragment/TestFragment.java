package com.orgazmpionerki.braintracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.braintracker.R;
import com.dkrasnov.util_android_lib.Tracer;
import com.dkrasnov.util_android_lib.taskexecutor.RequestExecutor;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTask;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.dataprovider.RequestDataServer;
import com.orgazmpionerki.braintracker.dataprovider.VideoData;
import com.orgazmpionerki.braintracker.dataprovider.YouTubeRequestDataServer;
import com.orgazmpionerki.braintracker.dataprovider.request.YouTubeGetPlaylistItemsRequest;
import com.orgazmpionerki.braintracker.dataprovider.request.YouTubeGetVideosInfoRequest;
import com.orgazmpionerki.braintracker.dataprovider.request.YouTubeGetWatchHistoryIdRequest;
import com.orgazmpionerki.braintracker.dataprovider.request.YouTubeProvider;
import com.orgazmpionerki.braintracker.outh2.GoogleAuthToken;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.wear.WearController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private boolean mFlag = false;
    private WearController mWearController;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    public TestFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWearController = new WearController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.test_fragment, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        layout.findViewById(R.id.test_button).setOnClickListener(view -> {
            mFlag = !mFlag;
            testYoutube();
        });


        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWearController.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWearController.disconnect();
    }

    @Override
    public void updateContent(Bundle args) {
    }

    RequestDataServer server;

    private void testYoutube() {
        if (server == null)
            server = new YouTubeRequestDataServer(getActivity());
        if (server.running()) {
            server.stop();
        } else {
            server.start();
        }
    }

    private void testWearConnection() {
        BrainTrackerDatabase database = new BrainTrackerDatabase(getActivity());
        database.open();

        mWearController.notifyPointsChanged(database.getBrainPoints(1));

        database.close();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}