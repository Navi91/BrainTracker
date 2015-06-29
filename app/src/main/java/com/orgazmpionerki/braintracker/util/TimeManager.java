package com.orgazmpionerki.braintracker.util;

import android.content.Context;

import java.util.Calendar;

public class TimeManager {

	public static int getNumberWorkDay(Context context) {
		long begin = Preferences.getBeginDate(context);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(begin);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		begin = calendar.getTimeInMillis();

		long today = Calendar.getInstance().getTimeInMillis();

		long diff = (today - begin) / (24 * 60 * 60 * 1000);
		int diff_days = (int) diff;
		diff_days++; // effect +-1

		return diff_days;
	}

    public static int getCurrentTimeInSeconds() {
        long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
        return (int) currentTime;
    }
}
