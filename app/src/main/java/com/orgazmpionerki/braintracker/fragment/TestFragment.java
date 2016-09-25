package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braintracker.R;
import com.dkrasnov.util_android_lib.ResultListener;
import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.dataprovider.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.datarequest.RequestDataServer;
import com.orgazmpionerki.braintracker.datarequest.YouTubeAlarmRequestDataServer;
import com.orgazmpionerki.braintracker.tracker.TrackEvent;
import com.orgazmpionerki.braintracker.tracker.TrackerListener;
import com.orgazmpionerki.braintracker.tracker.TrackerListenerImpl;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.wear.WearController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private boolean flag = false;
    private WearController mWearController;
    private Button startButton;
    private TrackerListener trackerListener;
    private TextView pointsTextView;

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
        server = YouTubeAlarmRequestDataServer.getInstance(getActivity());
        trackerListener = new TrackerListenerImpl(getActivity());
        trackerListener.addListener(result -> pointsTextView.setText(Integer.toString(result.changePoints)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.test_fragment, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        pointsTextView = (TextView) layout.findViewById(R.id.points);
        startButton = (Button) layout.findViewById(R.id.test_button);

        startButton.setOnClickListener(view -> {
            flag = !flag;
            testYoutube();
        });
        updateStartButton(!Preferences.getServerRunning(getActivity()));

        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWearController.connect();
        trackerListener.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWearController.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        trackerListener.onStop();
//        server.stop();
//        serviceController.stopService(getActivity());
    }

    @Override
    public void updateContent(Bundle args) {
    }

    RequestDataServer server;

    private void testYoutube() {
        if (server.running()) {
            server.stop();
        } else {
            server.start();
        }
        updateStartButton(!server.running());
    }

    private void updateStartButton(boolean start) {
        startButton.setText(start ? "Start test" : "Stop test");
    }

    private void testWearConnection() {
        BrainTrackerDatabaseImpl database = new BrainTrackerDatabaseImpl(getActivity());
        database.open();

        // TODO notification
//        mWearController.notifyPointsChanged(database.getBrainPoints(1));

        database.close();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}