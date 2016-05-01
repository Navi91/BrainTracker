package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.braintracker.R;
import com.orgazmpionerki.braintracker.activity.MainActivity;
import com.orgazmpionerki.braintracker.canvas.view.AnimationTextView;
import com.orgazmpionerki.braintracker.canvas.animator.PowerButtonAnimator;
import com.orgazmpionerki.braintracker.canvas.animator.ServiceStatusAnimator;
import com.orgazmpionerki.braintracker.service.BrainTrackerService;
import com.orgazmpionerki.braintracker.service.controllers.IBrainServiceController;
import com.orgazmpionerki.braintracker.service.controllers.ServiceController;

public class ServiceFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.service_fragment";

    private AnimationTextView mServiceStatusTextView;
    private AnimationTextView mPowerButton;

    private ServiceStatusAnimator mServiceStatusAnimator;
    private PowerButtonAnimator mPowerButtonAnimator;
    private IBrainServiceController mBrainServiceController;

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    public ServiceFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBrainServiceController = new ServiceController(BrainTrackerService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.f_service, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mPowerButton = (AnimationTextView) layout.findViewById(R.id.start_button);
        mPowerButtonAnimator = new PowerButtonAnimator(mPowerButton);

        mServiceStatusTextView = (AnimationTextView) layout.findViewById(R.id.service);
        mServiceStatusAnimator = new ServiceStatusAnimator(mServiceStatusTextView);

        mServiceStatusAnimator.setServiceStatus(mBrainServiceController.isServiceRunning(getActivity()));
        mPowerButtonAnimator.setState(mBrainServiceController.isServiceRunning(getActivity()));

        mPowerButtonAnimator.setClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBrainServiceController.isServiceRunning(getActivity())) {
                    stopService();
                } else {
                    startService();
                }
                updateContent(null);
            }
        });

        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent(null);
    }

    private void updateButtons(boolean serviceOn, AnimationTextView statusTextView, AnimationTextView powerButtonTextView) {
        statusTextView.setText(serviceOn ? R.string.service_on : R.string.service_off);
        powerButtonTextView.setText(serviceOn ? R.string.stop_service_button : R.string.start_service_button);
        mServiceStatusAnimator.setServiceStatus(serviceOn);
        mPowerButtonAnimator.setState(serviceOn);
    }

    public void startService() {
        ((MainActivity) getActivity()).attemptToStartBrainTrackerService();
    }

    public void stopService() {
        ((MainActivity) getActivity()).stopBrainTrackerService();
    }

    @Override
    public void updateContent(Bundle args) {
        updateButtons(mBrainServiceController.isServiceRunning(getActivity()), mServiceStatusTextView, mPowerButton);
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
