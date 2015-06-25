package com.braintracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.braintracker.MainActivity;
import com.braintracker.R;
import com.braintracker.controller.WifiController;
import com.braintracker.util.Preferences;
import com.braintracker.util.Tracer;

public class ServiceFragment extends BaseFragment {
	private static final String TAG = "com.braintracker.fargment.service_fragment";

	private boolean mServicesStarted = false;
	private Button mServiceButton;
	private TextView mServiceTextView;
	private TextView mWiFiTextView;

	public static ServiceFragment newInstance() {
		return new ServiceFragment();
	}

	public ServiceFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.service_fragment, null, true);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mServiceButton = (Button) layout.findViewById(R.id.service_button);
		mServiceTextView = (TextView) layout.findViewById(R.id.serice);
		mWiFiTextView = (TextView) layout.findViewById(R.id.wifi);

		mServiceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Tracer.debug("START BUTTON CLICK");
				if (Preferences.getIsServiceRunning(getActivity())) {
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

	private void updateButtons() {
		mServicesStarted = Preferences.getIsServiceRunning(getActivity());
		Tracer.debug("SERVICE " + mServicesStarted);

		mServiceButton.setText(mServicesStarted ? R.string.stop_service_button : R.string.start_service_button);
	}

	private void updateStatus() {
		boolean is_wifi_on = WifiController.isWifiConnected(getActivity());
		boolean is_service_work = Preferences.getIsServiceRunning(getActivity());

		mWiFiTextView.setText(is_wifi_on ? R.string.wifi_on : R.string.wifi_off);
		mWiFiTextView.setTextColor(is_wifi_on ? getResources().getColor(R.color.text_main) : getResources().getColor(R.color.text_secondary));

		mServiceTextView.setText(is_service_work ? R.string.service_on : R.string.service_off);
		mServiceTextView.setTextColor(is_service_work ? getResources().getColor(R.color.text_main) : getResources().getColor(R.color.text_secondary));
	}

	public void startService() {
		((MainActivity) getActivity()).attemptToStartBrainTrackerService();
	}

	public void stopService() {
		((MainActivity) getActivity()).stopBrainTrackerService();
	}

	@Override
	public void updateContent(Bundle args) {
		updateButtons();
		updateStatus();
	}

	@Override
	public String getFragmentTag() {
		return TAG;
	}
}
