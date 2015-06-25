package com.braintracker.database.table;

import android.database.sqlite.SQLiteDatabase;

public class WatchHistoryTable {

	// Database table
	public static final String TABLE_HISTORY = "history";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VIDEO_ID = "video_id";
	public static final String COLUMNN_TITLE = "title";
	public static final String COLUMNN_CATEGORY = "category";
	public static final String COLUMNN_LENGTH = "length";
	public static final String COLUMNN_POINTS = "points";
	public static final String COLUMNN_TIME = "time";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " + TABLE_HISTORY + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_VIDEO_ID + " text not null, " + COLUMNN_TITLE + " text not null, " + COLUMNN_CATEGORY + " text not null, " + COLUMNN_LENGTH + " integer not null, " + COLUMNN_POINTS + " integer not null, " + COLUMNN_TIME + " integer not null" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Upgrade to new database version
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		onCreate(database);
	}
}
