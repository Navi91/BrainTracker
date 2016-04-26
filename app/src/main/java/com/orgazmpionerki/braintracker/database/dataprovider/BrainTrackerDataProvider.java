package com.orgazmpionerki.braintracker.database.dataprovider;

import com.google.api.services.youtube.YouTube;
import com.orgazmpionerki.braintracker.dataprovider.VideoData;

import java.util.List;

/**
 * Created by Dmitriy on 26.04.2016.
 */
public interface BrainTrackerDataProvider {
    boolean haveVideo(VideoData videoData);

    boolean addVideo(VideoData videoData);

    boolean addVideoIfNotExist(VideoData videoData);

    List<VideoData> getLastVideos(int days);
}
