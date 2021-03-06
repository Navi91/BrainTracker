package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;

import com.braintracker.R;

public class SettingsFragment extends BaseFragment {
	public static final String TAG = "com.orgazmpionerki.braintracker.fragment.targets_fragment";

	public static SettingsFragment newInstance() {
		return new SettingsFragment();
	}

	public SettingsFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
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
