package com.orgazmpionerki.braintracker.database.tables;

import android.database.sqlite.SQLiteDatabase;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class DataResourceInfoTable {
    public static final String TABLE_RESOURCES = "resources";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAST_UPDATE_TIME = "last_update_time";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table " + TABLE_RESOURCES + "(" + COLUMN_ID + " string primary key not null, " + COLUMN_LAST_UPDATE_TIME + " integer not null);";

    public static void onCreate(SQLiteDatabase database) {
        Tracer.methodEnter(BrainTrackerDatabase.DEBUG_TAG);
        database.execSQL(DATABASE_CREATE);
    }

    // Upgrade to new database version
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Tracer.methodEnter(BrainTrackerDatabase.DEBUG_TAG);
        Tracer.debug(BrainTrackerDatabase.DEBUG_TAG, "onUpgrade old version = " + oldVersion + " new version = " + newVersion);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
        onCreate(database);
    }
}
