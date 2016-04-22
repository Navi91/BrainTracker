package com.orgazmpionerki.braintracker.dataprovider.request;

import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.orgazmpionerki.braintracker.dataprovider.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class WriteVideosToDatabaseRequest extends RequestTaskBase<List<VideoData>> {
    private List<VideoData> videoForWriteList;

    public WriteVideosToDatabaseRequest(List<VideoData> videoForWriteList, RequestTaskCallback<List<VideoData>> callback) {
        super(callback);
        init(videoForWriteList);
    }

    private void init(List<VideoData> videoToWriteList) {
        this.videoForWriteList = videoToWriteList;
    }

    @Override
    public List<VideoData> doRequest() throws Exception {
        List<VideoData> result = new ArrayList<>();

        for (VideoData videoData : videoForWriteList) {
            if (writeToDatabase(videoData)) {
                result.add(videoData);
            }
        }

        return result;
    }

    private boolean writeToDatabase(VideoData videoData) {
        // TODO work with db
        return true;
    }
}
