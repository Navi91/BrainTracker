package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.dataprovider.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.datarequest.RequestDataServer;
import com.orgazmpionerki.braintracker.datarequest.YouTubeRequestDataServer;
import com.orgazmpionerki.braintracker.wear.WearController;

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