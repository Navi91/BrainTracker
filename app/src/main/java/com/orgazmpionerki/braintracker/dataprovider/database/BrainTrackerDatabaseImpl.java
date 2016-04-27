package com.orgazmpionerki.braintracker.dataprovider.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.dataprovider.database.tables.WatchHistoryTable;
import com.orgazmpionerki.braintracker.util.TimeManager;

import java.util.Calendar;

public class BrainTrackerDatabaseImpl implements BrainTrackerDatabase {
    public final static String DEBUG_TAG = "database_debug";

    private SQLiteDatabase mDatabase;
    private BrainTrackerSQLiteHelper mDatabaseHelper;
    private boolean mOpen = false;
    private Context mContext;

    public BrainTrackerDatabaseImpl(Context context) {
        Tracer.methodEnter(DEBUG_TAG);
        mDatabaseHelper = new BrainTrackerSQLiteHelper(context);
        mContext = context;
    }

    @Override
    public void open() throws SQLException {
        Tracer.methodEnter(DEBUG_TAG);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        mOpen = true;
    }

    @Override
    public void close() {
        Tracer.methodEnter(DEBUG_TAG);
        mDatabaseHelper.close();
        mOpen = false;
    }

    @Override
    public boolean haveVideo(String resourceId, String videoId) {
        String table = WatchHistoryTable.TABLE_HISTORY;
        String resourceIdColumn = WatchHistoryTable.COLUMN_RESOURCE_ID;
        String videoIdColumn = WatchHistoryTable.COLUMN_VIDEO_ID;

        Cursor cursor = mDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?", table, resourceIdColumn, videoIdColumn), new String[]{resourceId, videoId});

        if (cursor == null) {
            return false;
        }

        boolean haveVideo = cursor.getCount() > 0;
        cursor.close();
        return haveVideo;
    }

    @Override
    public boolean writeVideo(String resourceId, String videoId, String category, String title, String length, int points) {
        ContentValues values = new ContentValues();
        values.put(WatchHistoryTable.COLUMN_RESOURCE_ID, resourceId);
        values.put(WatchHistoryTable.COLUMN_VIDEO_ID, videoId);
        values.put(WatchHistoryTable.COLUMN_TITLE, title);
        values.put(WatchHistoryTable.COLUMN_CATEGORY, category);
        values.put(WatchHistoryTable.COLUMN_LENGTH, length);
        values.put(WatchHistoryTable.COLUMN_POINTS, points);
        values.put(WatchHistoryTable.COLUMN_TIME, TimeManager.getCurrentTimeInSeconds());

        final int errorInsert = -1;
        return mDatabase.insert(WatchHistoryTable.TABLE_HISTORY, null, values) != errorInsert;
    }

    @Override
    public boolean deleteVideo(String resourceId, String videoId) {
        String table = WatchHistoryTable.TABLE_HISTORY;
        String resourceIdColumn = WatchHistoryTable.COLUMN_RESOURCE_ID;
        String videoIdColumn = WatchHistoryTable.COLUMN_VIDEO_ID;

        mDatabase.delete(table, String.format("WHERE %s = ? AND %s = ? ", resourceIdColumn, videoIdColumn), new String[]{resourceId, videoId});

        return false;
    }

    @Override
    public Cursor getLastVideos(long millisecondsAgo) {
        String table = WatchHistoryTable.TABLE_HISTORY;
        String columnTime = WatchHistoryTable.COLUMN_TIME;

        long today = Calendar.getInstance().getTimeInMillis();
        long startDate = today - millisecondsAgo;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long selectDate = calendar.getTimeInMillis() / 1000;

        return mDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s > ?", table, columnTime), new String[]{String.valueOf(selectDate)});
    }
}
