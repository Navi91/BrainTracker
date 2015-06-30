package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.MainActivity;
import com.orgazmpionerki.braintracker.canvas.AnimationTextView;
import com.orgazmpionerki.braintracker.canvas.PowerButtonAnimator;
import com.orgazmpionerki.braintracker.canvas.ServiceStatusAnimator;
import com.orgazmpionerki.braintracker.service.controllers.BrainTrackerServiceController;
import com.orgazmpionerki.braintracker.service.controllers.IBrainServiceController;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private AnimationTextView mServiceStatusTextView;
    private AnimationTextView mPowerButton;

    private ServiceStatusAnimator mServiceStatusAnimator;
    private PowerButtonAnimator mPowerButtonAnimator;
    private IBrainServiceController mBrainServiceController;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    public TestFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBrainServiceController = new BrainTrackerServiceController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.test_fragment, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));



        Button testButton = (Button) layout.findViewById(R.id.test_button);

        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void updateContent(Bundle args) {
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}