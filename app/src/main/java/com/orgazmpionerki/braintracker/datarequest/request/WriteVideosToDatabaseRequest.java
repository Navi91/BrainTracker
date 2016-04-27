package com.orgazmpionerki.braintracker.datarequest.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.orgazmpionerki.braintracker.dataprovider.BrainTrackerDataProvider;
import com.orgazmpionerki.braintracker.dataprovider.BrainTrackerDataProviderImpl;
import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class WriteVideosToDatabaseRequest extends RequestTaskBase<List<VideoData>> {
    private List<VideoData> videoForWriteList;
    private Context context;
    private BrainTrackerDataProvider dataProvider;

    public WriteVideosToDatabaseRequest(Context context, List<VideoData> videoForWriteList, RequestTaskCallback<List<VideoData>> callback) {
        super(callback);
        init(context, videoForWriteList);
    }

    private void init(Context context, List<VideoData> videoToWriteList) {
        this.videoForWriteList = videoToWriteList;
        this.context = context;
        dataProvider = BrainTrackerDataProviderImpl.getInstance(context);
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
        return dataProvider.addVideoIfNotExist(videoData);
    }
}
