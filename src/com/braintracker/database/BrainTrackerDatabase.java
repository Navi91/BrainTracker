package com.braintracker.database;

import java.util.Calendar;

import com.braintracker.database.table.WatchHistoryTable;
import com.braintracker.util.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BrainTrackerDatabase {
	private SQLiteDatabase mDatabase;
	private BrainTrackerSQLiteHelper mDatabaseHelper;

	public BrainTrackerDatabase(Context context) {
		mDatabaseHelper = new BrainTrackerSQLiteHelper(context);
	}

	public void open() throws SQLException {
		mDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void close() {
		mDatabaseHelper.close();
	}

	public boolean addVideoToWatchHistory(String video_id, String title, String category, int length, int points, long time) {
		// Tracer.debug("TRY ADD VIDEO LENGTH " + length);

		if (hasVideoInHistory(video_id)) {
			return false;
		}

		ContentValues values = new ContentValues();
		values.put(WatchHistoryTable.COLUMN_VIDEO_ID, video_id);
		values.put(WatchHistoryTable.COLUMNN_TITLE, title);
		values.put(WatchHistoryTable.COLUMNN_CATEGORY, category);
		values.put(WatchHistoryTable.COLUMNN_LENGTH, length);
		values.put(WatchHistoryTable.COLUMNN_POINTS, points);
		values.put(WatchHistoryTable.COLUMNN_TIME, time);

		mDatabase.insert(WatchHistoryTable.TABLE_HISTORY, null, values);

		return true;
	}

	private boolean hasVideoInHistory(String video_id) {
		String table = WatchHistoryTable.TABLE_HISTORY;
		String column_id = WatchHistoryTable.COLUMN_VIDEO_ID;

		Cursor cursor = mDatabase.rawQuery("select " + column_id + " FROM " + table + " WHERE " + column_id + " = ?", new String[] { video_id });

		if (cursor.getCount() != 0) {
			cursor.close();
			return true;
		}

		cursor.close();

		return false;
	}

	public int getBrainPoints(int days) {
		String table = WatchHistoryTable.TABLE_HISTORY;
		String column_points = WatchHistoryTable.COLUMNN_POINTS;
		String column_time = WatchHistoryTable.COLUMNN_TIME;

		int result = 0;
		long today = Calendar.getInstance().getTimeInMillis();
		long start_date = today - (days - 1) * Constants.DAY_IN_MILLIS;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start_date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		long select_date = calendar.getTimeInMillis();

		Cursor cursor = mDatabase.rawQuery("select " + column_points + " FROM " + table + " WHERE " + column_time + " > ?", new String[] { "" + select_date });
		int points_index = cursor.getColumnIndex(column_points);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			result += cursor.getInt(points_index);
			cursor.moveToNext();
		}

		cursor.close();

		return result;
	}

	// private boolean hasVideoInHistory(String video_id, long time) {
	// String table = WatchHistoryTable.TABLE_HISTORY;
	// String column_id = WatchHistoryTable.COLUMN_VIDEO_ID;
	// String column_time = "" + WatchHistoryTable.COLUMNN_TIME;
	//
	// Cursor cursor = mDatabase.rawQuery("select " + column_id + "," + column_time + " FROM " + table + " WHERE " + column_id + " = ? AND " + column_time + " = ?", new String[] { video_id, "" + time });
	//
	// if (cursor.getCount() == 0) {
	// return true;
	// }
	//
	// cursor.close();
	//
	// return false;
	// }
}
