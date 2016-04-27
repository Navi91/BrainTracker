package com.orgazmpionerki.braintracker.datarequest.request;

import android.content.Context;
import android.text.TextUtils;

import com.dkrasnov.util_android_lib.Tracer;
import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.orgazmpionerki.braintracker.dataprovider.BrainTrackerDataProvider;
import com.orgazmpionerki.braintracker.dataprovider.BrainTrackerDataProviderImpl;
import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class YouTubeGetNewVideoRequest extends RequestTaskBase<List<VideoData>> {
    private Context context;
    private BrainTrackerDataProvider dataProvider;

    public YouTubeGetNewVideoRequest(Context context, HandleErrorRequestCallback<List<VideoData>> callback) {
        super(callback);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        dataProvider = BrainTrackerDataProviderImpl.getInstance(context);
    }

    @Override
    public List<VideoData> doRequest() throws Exception {
        String historyId = Preferences.getHistoryId(context);

        if (TextUtils.isEmpty(historyId)) {
            historyId = new YouTubeGetWatchHistoryIdRequest(context).execute();
            Preferences.setHistoryId(context, historyId);
        }

        if (TextUtils.isEmpty(historyId)) {
            throw new IllegalStateException("Watch history id is empty!!!");
        }

        List<String> lastHistoryVideoIdes = new YouTubeGetPlaylistItemsRequest(context, historyId, 10, null).execute();
        List<VideoData> videos = new YouTubeGetVideosInfoRequest(lastHistoryVideoIdes, context, null).execute();
        Tracer.debug("Last videos: \n " + videos.toString());
        List<VideoData> result = new ArrayList<>();

        for (VideoData videoData : videos) {
            if (isNewVideo(videoData)) {
                result.add(videoData);
            }
        }
        Tracer.debug("New videos: \n " + result.toString());

        result = new WriteVideosToDatabaseRequest(context, result, null).execute();
        return result;
    }

    private boolean isNewVideo(VideoData videoData) {
        return !dataProvider.haveVideo(videoData);
    }
}
