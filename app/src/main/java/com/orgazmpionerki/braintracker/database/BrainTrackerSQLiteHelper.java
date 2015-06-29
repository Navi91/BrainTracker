package com.orgazmpionerki.braintracker.database;

import com.orgazmpionerki.braintracker.database.tables.DataResourceInfoTable;
import com.orgazmpionerki.braintracker.database.tables.WatchHistoryTable;
import com.orgazmpionerki.braintracker.util.Tracer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BrainTrackerSQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "braintracker.db";
	private static final int DATABASE_VERSION = 2;

	public BrainTrackerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        Tracer.methodEnter(BrainTrackerDatabase.DEBUG_TAG);
		WatchHistoryTable.onCreate(db);
        DataResourceInfoTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Tracer.methodEnter(BrainTrackerDatabase.DEBUG_TAG);
		WatchHistoryTable.onUpgrade(db, oldVersion, newVersion);
        DataResourceInfoTable.onUpgrade(db, oldVersion, newVersion);
	}
}
