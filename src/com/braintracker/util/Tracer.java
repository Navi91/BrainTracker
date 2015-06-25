package com.braintracker.util;

import android.util.Log;

public class Tracer {
	private static final String BRAIN_TRACKER_TAG = "brain_tracker";

	public static void debug(String tag, String message) {
		Log.w(tag, message);
	}

	public static void debug(String message) {
		Log.w(BRAIN_TRACKER_TAG, message);
	}

	public static void error(String message, Throwable e) {
		android.util.Log.e(BRAIN_TRACKER_TAG, Thread.currentThread().getName() + "| " + message, e);
	}
}
