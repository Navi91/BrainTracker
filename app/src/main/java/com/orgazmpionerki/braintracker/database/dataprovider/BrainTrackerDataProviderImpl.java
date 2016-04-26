package com.orgazmpionerki.braintracker.database.dataprovider;

import android.content.Context;
import android.database.SQLException;

import com.crashlytics.android.core.CrashlyticsCore;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.dataprovider.VideoData;

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

        return null;
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
