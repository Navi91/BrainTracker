package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.dataprovider.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.datarequest.RequestDataServer;
import com.orgazmpionerki.braintracker.datarequest.YouTubeAlarmRequestDataServer;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.wear.WearController;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private boolean flag = false;
    private WearController mWearController;
    private Button startButton;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.test_fragment, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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
    }

    @Override
    public void onPause() {
        super.onPause();
        mWearController.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
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