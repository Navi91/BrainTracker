package com.orgazmpionerki.braintracker.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public abstract class BaseFragment extends PreferenceFragment {
	public void updateContent(Bundle args) {}

	public abstract String getFragmentTag();
}
