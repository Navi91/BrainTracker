package com.braintracker.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.braintracker.R;
import com.braintracker.adapter.BrainTrackerAdapter;

public class BrainTrackerPager extends LinearLayout {
	private ViewPager mViewPager;
	private BrainTrackerAdapter mAdapter;
	private HorizontalScrollView mButtonsScrollView;
	private int mSelectedPage = 1;
	private FragmentActivity mActivity;
	private List<PageButton> mButtons;

	private class PageButton {
		private View mView;
		private int mId;

		private PageButton(View v, int id) {
			mView = v;
			mId = id;
		}

		private View getView() {
			return mView;
		}

		private int getId() {
			return mId;
		}

		private void setUnselectedState() {
			((Button) mView).setTextColor(getResources().getColor(R.color.white));
			((Button) mView).setTypeface(null, Typeface.NORMAL);
		}

		private void setSelectedState() {
			((Button) mView).setTextColor(getResources().getColor(R.color.accent_main));
			((Button) mView).setTypeface(null, Typeface.BOLD);
		}
	}

	private OnClickListener mPageButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mSelectedPage = getPageIndx(v);
			updatePager();
		}
	};

	public BrainTrackerPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initContent(context);
	}

	public BrainTrackerPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initContent(context);
	}

	public BrainTrackerPager(Context context) {
		super(context, null);
		initContent(context);
	}

	public void updateFragmentsContent(Bundle args) {
		if (mAdapter != null) {
			mAdapter.updateFragments(args);
		}
	}

	private void initContent(Context context) {
		mActivity = (FragmentActivity) context;

		LayoutInflater factory = LayoutInflater.from(mActivity);
		factory.inflate(R.layout.brain_tracker_pager, this);

		initViewPager();
		initButtons();
		initButtonsScrollView();

		LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(param);

		updatePager();
	}

	private void updatePager() {
		updateButtons();
		updateViewPager();
	}

	private void updateButtons() {
		for (PageButton button : mButtons) {
			if (button.getId() == mSelectedPage) {
				button.setSelectedState();
			} else {
				button.setUnselectedState();
			}
		}
	}

	private void updateViewPager() {
		mViewPager.setCurrentItem(mSelectedPage);
	}

	private int getPageIndx(View v) {
		int indx = 0;
		for (PageButton button : mButtons) {
			if (v == button.getView()) {
				return indx;
			}
			indx++;
		}
		return 0;
	}

	private void adjustPagePosition() {
		if (mButtons == null) {
			return;
		}
		int offsetX = ((mButtons.get(mSelectedPage).getView().getLeft() + mButtons.get(mSelectedPage).getView().getRight()) / 2) - (mButtonsScrollView.getWidth() / 2);
		mButtonsScrollView.scrollTo(offsetX, 0);
	}

	private void initButtons() {
		mButtons = new ArrayList<PageButton>();

		mButtons.add(new PageButton(findViewById(R.id.chart_button), 0));
		mButtons.add(new PageButton(findViewById(R.id.service_button), 1));
		mButtons.add(new PageButton(findViewById(R.id.targets_button), 2));

		for (PageButton button : mButtons) {
			button.getView().setOnClickListener(mPageButtonClickListener);
		}
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mAdapter = new BrainTrackerAdapter(mActivity.getFragmentManager());
		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (position >= 0 && position < mButtons.size()) {
					mSelectedPage = position;
					updatePager();
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageScrollStateChanged(int state) {}
		});
	}

	private void initButtonsScrollView() {
		mButtonsScrollView = (HorizontalScrollView) findViewById(R.id.buttons_scroll_view);

		mButtonsScrollView.post(new Runnable() {

			@Override
			public void run() {
				adjustPagePosition();
			}
		});
	}
}
