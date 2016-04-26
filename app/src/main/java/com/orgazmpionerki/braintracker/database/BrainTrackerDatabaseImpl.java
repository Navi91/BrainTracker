package com.orgazmpionerki.braintracker.database;

import java.util.Calendar;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.database.tables.DataResourceInfoTable;
import com.orgazmpionerki.braintracker.database.tables.WatchHistoryTable;
import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;
import com.orgazmpionerki.braintracker.datasource.dataresource.ResourceType;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.util.TimeManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public void open() throws SQLException {
        Tracer.methodEnter(DEBUG_TAG);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        mOpen = true;
    }

    public void close() {
        Tracer.methodEnter(DEBUG_TAG);
        mDatabaseHelper.close();
        mOpen = false;
    }

    public void updateResourceLastUpdateTime(ResourceType type) {
        Tracer.methodEnter(DEBUG_TAG);
        Cursor cursor = mDatabase.rawQuery("select " + DataResourceInfoTable.COLUMN_ID + " FROM " + DataResourceInfoTable.TABLE_RESOURCES + " WHERE " + DataResourceInfoTable.COLUMN_ID + " = ?", new String[]{type.getId()});
        if (cursor == null || cursor.getCount() == 0) {
            addDataResourceInfo(type);
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(DataResourceInfoTable.COLUMN_ID, type.getId());
        values.put(DataResourceInfoTable.COLUMN_LAST_UPDATE_TIME, TimeManager.getCurrentTimeInSeconds());

        mDatabase.update(DataResourceInfoTable.TABLE_RESOURCES, values, DataResourceInfoTable.COLUMN_ID + " = ?", new String[]{type.getId()});
    }

    public int getResourceLastUpdateTime(ResourceType type) {
        Tracer.methodEnter(DEBUG_TAG);
        Cursor cursor = mDatabase.rawQuery("select " + DataResourceInfoTable.COLUMN_LAST_UPDATE_TIME + " FROM " + DataResourceInfoTable.TABLE_RESOURCES + " WHERE " + DataResourceInfoTable.COLUMN_ID + " = ?", new String[]{type.getId()});

        if (cursor == null || cursor.getCount() == 0) {
            addDataResourceInfo(type);
            cursor.close();
            return -1;
        }

        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex(DataResourceInfoTable.COLUMN_LAST_UPDATE_TIME));
        cursor.close();
        return result;
    }

    private void addDataResourceInfo(ResourceType type) {
        Tracer.methodEnter(DEBUG_TAG);
        ContentValues values = new ContentValues();
        values.put(DataResourceInfoTable.COLUMN_ID, type.getId());
        values.put(DataResourceInfoTable.COLUMN_LAST_UPDATE_TIME, TimeManager.getCurrentTimeInSeconds());

        mDatabase.insert(DataResourceInfoTable.TABLE_RESOURCES, null, values);
    }

    public boolean addVideoToWatchHistory(IDataElement element) {
        return addVideoToWatchHistory(element.getId(), element.getName(), "", element.getLength(), element.getBrainPoints());
    }

    public boolean addVideoToWatchHistory(String video_id, String title, String category, int length, int points) {
        if (hasVideoInHistory(video_id)) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(WatchHistoryTable.COLUMN_VIDEO_ID, video_id);
        values.put(WatchHistoryTable.COLUMN_TITLE, title);
        values.put(WatchHistoryTable.COLUMN_CATEGORY, category);
        values.put(WatchHistoryTable.COLUMN_LENGTH, length);
        values.put(WatchHistoryTable.COLUMN_POINTS, points);
        values.put(WatchHistoryTable.COLUMN_TIME, TimeManager.getCurrentTimeInSeconds());

        mDatabase.insert(WatchHistoryTable.TABLE_HISTORY, null, values);

        return true;
    }

    private boolean hasVideoInHistory(String video_id) {
        String table = WatchHistoryTable.TABLE_HISTORY;
        String column_id = WatchHistoryTable.COLUMN_VIDEO_ID;

        Cursor cursor = mDatabase.rawQuery("select " + column_id + " FROM " + table + " WHERE " + column_id + " = ?", new String[]{video_id});

        if (cursor.getCount() != 0) {
            cursor.close();
            return true;
        }

        cursor.close();

        return false;
    }

    @Override
    public Cursor getLastVideos(int secondsAgo) {
        String table = WatchHistoryTable.TABLE_HISTORY;
        String columnPoints = WatchHistoryTable.COLUMN_POINTS;
        String columnTime = WatchHistoryTable.COLUMN_TIME;

        long today = Calendar.getInstance().getTimeInMillis();



        return null;
    }

    public int getBrainPoints(int days) {
        Tracer.methodEnter(DEBUG_TAG);

        if (days < 1) {
            throw  new UnsupportedOperationException("The days count must be > 0!!!");
        }

        String table = WatchHistoryTable.TABLE_HISTORY;
        String columnPoints = WatchHistoryTable.COLUMN_POINTS;
        String columnTime = WatchHistoryTable.COLUMN_TIME;

        int result = 0;
        // add every day brain points
        result += days * Preferences.getTargetValue(mContext);

        long today = Calendar.getInstance().getTimeInMillis();
        long startDate = today - (days - 1) * Constants.DAY_IN_MILLIS;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long select_date = calendar.getTimeInMillis() / 1000;

        Cursor cursor = mDatabase.rawQuery("select " + columnPoints + " FROM " + table + " WHERE " + columnTime + " > ?", new String[]{"" + select_date});
        int points_index = cursor.getColumnIndex(columnPoints);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            result += cursor.getInt(points_index);
            cursor.moveToNext();
        }

        cursor.close();

        return result;
    }

    public boolean isOpen() {
        return mOpen;
    }

    @Override
    public boolean haveVideo(String resourceId, String videoId) {
        return false;
    }

    @Override
    public boolean writeVideo(String resourceId, String videoId, String category, String title, String length, int points) {
        return false;
    }

    @Override
    public boolean deleteVideo(String resourceId, String videoId) {
        return false;
    }
}
