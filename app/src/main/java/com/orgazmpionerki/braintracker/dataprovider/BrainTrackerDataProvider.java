package com.orgazmpionerki.braintracker.dataprovider;

import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;

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
