package com.braintracker.database;

import com.braintracker.database.table.WatchHistoryTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BrainTrackerSQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "braintracker.db";
	private static final int DATABASE_VERSION = 1;

	public BrainTrackerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		WatchHistoryTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		WatchHistoryTable.onUpgrade(db, oldVersion, newVersion);
	}
}
