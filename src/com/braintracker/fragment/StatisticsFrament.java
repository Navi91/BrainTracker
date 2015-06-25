package com.braintracker.fragment;

import java.util.Calendar;

import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.braintracker.R;
import com.braintracker.database.BrainTrackerDatabase;
import com.braintracker.util.Preferences;
import com.braintracker.util.TimeManager;
import com.braintracker.util.Tracer;
import com.braintracker.view.ValueTextView;

public class StatisticsFrament extends BaseFragment {
	private static final String TAG = "com.braintracker.fragment.charts_fragment";

	private final String[] mPeriods = new String[] { "Day", "Week", "Month" };

	private ArrayAdapter<String> mAdapter;
	private Spinner mSpinner;
	private ValueTextView mPoints;
	private ValueTextView mTarget;
	private ValueTextView mPercents;
	private StatisticsPeriod mPeriod;
	private int mTargetValue;
	private int mPointsValue;
	private BrainTrackerDatabase mDatabase;

	public enum StatisticsPeriod {
		DAY, WEEK, MONTH
	}

	public static StatisticsFrament newInstance() {
		return new StatisticsFrament();
	}

	public StatisticsFrament() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mDatabase == null) {
			mDatabase = new BrainTrackerDatabase(getActivity());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.chapters_fragment, null, false);

		mSpinner = (Spinner) layout.findViewById(R.id.spinner);
		mTarget = new ValueTextView(getResources().getString(R.string.statistics_target_caption), (TextView) layout.findViewById(R.id.target));
		mPoints = new ValueTextView(getResources().getString(R.string.statistics_points_caption), (TextView) layout.findViewById(R.id.points));
		mPercents = new ValueTextView(getResources().getString(R.string.statistics_percents_caption), (TextView) layout.findViewById(R.id.percents));

		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mPeriods);

		mAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		mSpinner.setAdapter(mAdapter);
		mSpinner.setSelection(Preferences.getStatisticsPeriod(getActivity()));
		updateStatisticsPeriod(Preferences.getStatisticsPeriod(getActivity()));

		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateStatisticsPeriod(position);
				Preferences.putStatisticsPeriod(getActivity(), position);

				updateContent(null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		// TODO charts in future

		setRetainInstance(false);
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateContent(null);
	}

	@Override
	public String getFragmentTag() {
		return TAG;
	}

	@Override
	public void updateContent(Bundle args) {
		updateTarget();
		updatePoints();
		updatePercents();
	}

	private void updateTarget() {
		int target = Preferences.getTargetValue(getActivity());

		switch (mPeriod) {
		case DAY:
			target *= 1;
			break;
		case WEEK:
			target *= Math.min(7, TimeManager.getNumberWorkDay(getActivity()));
			break;
		case MONTH:
			int day_in_month = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
			target *= Math.min(day_in_month, TimeManager.getNumberWorkDay(getActivity()));
			break;
		default:
			break;
		}

		mTargetValue = target;

		if (mTargetValue > 0) {
			mTarget.setValue("+ " + mTargetValue);
		} else {
			mTarget.setValue("" + mTargetValue);
		}
	}

	private void updatePoints() {
		int points = 1;

		try {
			mDatabase.open();

			switch (mPeriod) {
			case DAY:
				points = mDatabase.getBrainPoints(1);
				break;
			case WEEK:
				points = mDatabase.getBrainPoints(7);
				break;
			case MONTH:
				int day_in_month = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
				points = mDatabase.getBrainPoints(day_in_month);
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			Tracer.error("BRAIN TRACKER ERROR", e);
		} finally {
			mDatabase.close();
		}

		mPointsValue = points;

		if (points > 0) {
			mPoints.setValue("+ " + mPointsValue);
		} else {
			mPoints.setValue("" + mPointsValue);
		}
	}

	private void updatePercents() {
		int percents;

		if (mTargetValue == 0) {
			return;
		}

		percents = (100 * mPointsValue) / mTargetValue;

		if (mTargetValue < 0) {
			percents *= -1;
		}

		if (percents > 0) {
			mPercents.setValue("+ " + percents + " %");
		} else {
			mPercents.setValue("" + percents + " %");
		}
	}

	private void updateStatisticsPeriod(int period) {
		switch (period) {
		case 0:
			mPeriod = StatisticsPeriod.DAY;
			break;
		case 1:
			mPeriod = StatisticsPeriod.WEEK;
			break;
		case 2:
			mPeriod = StatisticsPeriod.MONTH;
			break;
		default:
			break;
		}
	}
}