package com.orgazmpionerki.braintracker.dataprovider;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.crashlytics.android.core.CrashlyticsCore;
import com.orgazmpionerki.braintracker.dataprovider.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.dataprovider.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.dataprovider.database.tables.WatchHistoryTable;
import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;
import com.orgazmpionerki.braintracker.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 26.04.2016.
 */
public class BrainTrackerDataProviderImpl implements BrainTrackerDataProvider {
    private static BrainTrackerDataProvider sInstance;

    private BrainTrackerDatabase database;

    public static BrainTrackerDataProvider getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BrainTrackerDataProviderImpl(context);
        }

        return sInstance;
    }

    private BrainTrackerDataProviderImpl(Context context) {
        database = new BrainTrackerDatabaseImpl(context);
    }

    @Override
    public synchronized boolean haveVideo(VideoData videoData) {
        if (!openDatabase()) {
            return false;
        }

        boolean result = database.haveVideo(videoData.resourceId, videoData.id);

        closeDatabase();
        return result;
    }

    @Override
    public synchronized boolean addVideoIfNotExist(VideoData videoData) {
        if (!haveVideo(videoData)) {
            return addVideo(videoData);
        }

        return false;
    }

    @Override
    public synchronized boolean addVideo(VideoData videoData) {
        if (!openDatabase()) {
            return false;
        }

        boolean result = database.writeVideo(videoData.resourceId, videoData.id, videoData.category, videoData.title, videoData.length, videoData.points);

        closeDatabase();
        return result;
    }

    @Override
    public synchronized List<VideoData> getLastVideos(int days) {
        List<VideoData> result = new ArrayList<>();

        if (!openDatabase() || days < 1) {
            return result;
        }

        long secondsAgo = (days - 1) * Constants.DAY_IN_MILLIS;
        Cursor cursor = database.getLastVideos(secondsAgo);

        if (cursor == null) {
            return result;
        }

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            result.add(createVideoDataFrom(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return result;
    }

    private VideoData createVideoDataFrom(Cursor cursor) {
        int resourceIdIndex = cursor.getColumnIndex(WatchHistoryTable.COLUMN_RESOURCE_ID);
        int videoIdIndex = cursor.getColumnIndex(WatchHistoryTable.COLUMN_VIDEO_ID);
        int categoryIndex = cursor.getColumnIndex(WatchHistoryTable.COLUMN_CATEGORY);
        int titleIndex = cursor.getColumnIndex(WatchHistoryTable.COLUMN_TITLE);
        int lengthIndex = cursor.getColumnIndex(WatchHistoryTable.COLUMN_LENGTH);
        int pointsIndex = cursor.getColumnIndex(WatchHistoryTable.COLUMN_POINTS);

        VideoData videoData = new VideoData();
        videoData.resourceId = cursor.getString(resourceIdIndex);
        videoData.id = cursor.getString(videoIdIndex);
        videoData.category = cursor.getString(categoryIndex);
        videoData.title = cursor.getString(titleIndex);
        videoData.length = cursor.getString(lengthIndex);
        videoData.points = cursor.getInt(pointsIndex);

        return videoData;
    }

    private boolean openDatabase() {
        try {
            database.open();
        } catch (SQLException e) {
            e.printStackTrace();
            CrashlyticsCore.getInstance().logException(e);
            return false;
        }

        return true;
    }

    private void closeDatabase() {
        database.close();
    }
}
