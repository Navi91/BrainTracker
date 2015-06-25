package com.braintracker.view;

import android.widget.TextView;

public class ValueTextView {
	private String mCaption;
	private String mValue;
	private TextView mTextView;

	public ValueTextView(String caption, TextView text_view) {
		mCaption = caption;
		mTextView = text_view;
		mTextView.setText(mCaption);
	}

	public void setValue(String value) {
		mValue = value;
		mTextView.setText(mCaption + " " + mValue);
	}
}
