package com.orgazmpionerki.braintracker.database;

import android.database.Cursor;
import android.database.SQLException;

/**
 * Created by Dmitriy on 26.04.2016.
 */
public interface BrainTrackerDatabase {
    void open() throws SQLException;

    void close();

    boolean haveVideo(String resourceId, String videoId);

    boolean writeVideo(String resourceId, String videoId, String category, String title, String length, int points);

    boolean deleteVideo(String resourceId, String videoId);

    Cursor getLastVideos(int secondsAgo);
}
