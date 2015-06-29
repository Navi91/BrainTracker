package com.orgazmpionerki.braintracker.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class MetricsConverter {

	public static int getVideoLengthInMinutes(String length_string) {
		int length = 0;
		int hours = 0;
		int minutes = 0;

		try {
			length_string = length_string.substring(2, length_string.length());
		} catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
			return length;
		}

		// get index of hours
		int hour_index = length_string.indexOf("H");

		if (hour_index != -1) {
			// get count of hours in length_string
			hours = Integer.parseInt(length_string.substring(0, hour_index));
			length += 60 * hours;

			try {
				length_string = length_string.substring(hour_index + 1, length_string.length());
			} catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
				return length;
			}
		}

		// get minute index
		int minute_index = length_string.indexOf("M");

		if (minute_index != -1) {
			// get count of minutes in length_string
			minutes = Integer.parseInt(length_string.substring(0, minute_index));
		}

		length += minutes;

		return length;
	}

	public static int getBrainPoints(Context context, String category, int length) throws JSONException {
		JSONObject category_points;
		category_points = new JSONObject(Preferences.getCategory(context));
		int brain_points = length * category_points.getInt(category);

		return brain_points;
	}
}
