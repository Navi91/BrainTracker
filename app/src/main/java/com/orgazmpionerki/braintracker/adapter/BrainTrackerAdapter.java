package com.orgazmpionerki.braintracker.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.orgazmpionerki.braintracker.fragment.BaseFragment;
import com.orgazmpionerki.braintracker.fragment.StatisticsFrament;
import com.orgazmpionerki.braintracker.fragment.ServiceFragment;
import com.orgazmpionerki.braintracker.fragment.SettingsFragment;

public class BrainTrackerAdapter extends FragmentStatePagerAdapter {
	private BaseFragment[] mFragments;
	private int mCount = 3;

	public BrainTrackerAdapter(FragmentManager fm) {
		super(fm);

		mFragments = new BaseFragment[mCount];
	}

	@Override
	public Fragment getItem(int id) {
		switch (id) {
		case 0:
			mFragments[id] = getChartFragment();
			break;
		case 1:
			mFragments[id] = getServiceFragment();
			break;
		case 2:
			mFragments[id] = getTargetFragment();
			break;
		}

		return mFragments[id];
	}

	@Override
	public int getCount() {
		return mCount;
	}

	public void updateFragments(Bundle args) {
		for (int i = 0; i < mFragments.length; i++) {
			if (mFragments[i] != null) {
				mFragments[i].updateContent(args);
			}
		}
	}

	private BaseFragment getChartFragment() {
		BaseFragment fragment = StatisticsFrament.newInstance();

		return fragment;
	}

	private BaseFragment getServiceFragment() {
		BaseFragment fragment = ServiceFragment.newInstance();

		return fragment;
	}

	private BaseFragment getTargetFragment() {
		BaseFragment fragment = SettingsFragment.newInstance();

		return fragment;
	}
}
