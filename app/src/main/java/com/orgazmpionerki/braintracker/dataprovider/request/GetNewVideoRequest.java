package com.orgazmpionerki.braintracker.dataprovider.request;

import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.orgazmpionerki.braintracker.dataprovider.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class GetNewVideoRequest extends RequestTaskBase<List<VideoData>> {
    private List<VideoData> videos;

    public GetNewVideoRequest(List<VideoData> videos, RequestTaskCallback<List<VideoData>> callback) {
        super(callback);
        init(videos);
    }

    private void init(List<VideoData> videos) {
        this.videos = videos;
    }

    @Override
    public List<VideoData> doRequest() throws Exception {
        List<VideoData> result = new ArrayList<>();

        for (VideoData videoData : videos) {
            if (isNewVideo(videoData)) {
                result.add(videoData);
            }
        }

        return result;
    }

    private boolean isNewVideo(VideoData videoData) {
        // TODO work with DB
        return true;
    }
}
