package com.orgazmpionerki.braintracker.dataprovider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.dataprovider.database.tables.WatchHistoryTable;

public class BrainTrackerSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "braintracker.db";
    private static final int DATABASE_VERSION = 2;

    public BrainTrackerSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Tracer.debug(BrainTrackerDatabaseImpl.DEBUG_TAG, "onCreate");
        Tracer.methodEnter(BrainTrackerDatabaseImpl.DEBUG_TAG);
        WatchHistoryTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Tracer.debug(BrainTrackerDatabaseImpl.DEBUG_TAG, "onUpgrade " + oldVersion + " " + newVersion);
        Tracer.methodEnter(BrainTrackerDatabaseImpl.DEBUG_TAG);
        WatchHistoryTable.onUpgrade(db, oldVersion, newVersion);
    }
}
