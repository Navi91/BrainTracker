package com.braintracker.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.braintracker.R;

public class TargetSetting extends DialogPreference {
	private int mCurrentValue;
	private int mNewValue;
	private int mDefaultValue;
	private EditText mEditText;

	public TargetSetting(Context context, AttributeSet attrs) {
		super(context, attrs);

		mDefaultValue = context.getResources().getInteger(R.integer.target_default_value);

		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

		setDialogIcon(null);
	}

	@Override
	protected View onCreateDialogView() {
		LinearLayout layout = (LinearLayout) LinearLayout.inflate(getContext(), R.layout.target_value_setting_dialog, null);
		mEditText = (EditText) layout.findViewById(R.id.edit);

		mEditText.post(new Runnable() {

			@Override
			public void run() {
				InputMethodManager input_manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				input_manager.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
			}
		});

		return layout;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			mNewValue = getNewValue(mEditText.getText().toString());
			persistInt(mNewValue);
			mCurrentValue = mNewValue;
			updateSummary(mNewValue);
		}
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			// Restore existing state
			mCurrentValue = getPersistedInt(mDefaultValue);
		} else {
			// Set default state from the XML attribute
			mCurrentValue = (Integer) defaultValue;
			persistInt(mCurrentValue);
		}

		updateSummary(mCurrentValue);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, mDefaultValue);
	}

	private int getNewValue(String text) {
		int res = 0;

		try {
			res = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return mCurrentValue;
		}

		return res;
	}

	private void updateSummary(int points) {
		setSummary(points + " brain points");
	}
}
