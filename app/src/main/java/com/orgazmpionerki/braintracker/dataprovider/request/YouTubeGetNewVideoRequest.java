package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;
import android.text.TextUtils;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.orgazmpionerki.braintracker.dataprovider.VideoData;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class YouTubeGetNewVideoRequest extends RequestTaskBase<List<VideoData>> {
    private Context context;

    public YouTubeGetNewVideoRequest(Context context, HandleErrorRequestCallback<List<VideoData>> callback) {
        super(callback);
        init(context);
    }

    private void init( Context context) {
        this.context = context;
    }

    @Override
    public List<VideoData> doRequest() throws Exception {
        String historyId = Preferences.getHistoryId(context);

        if (TextUtils.isEmpty(historyId)) {
            historyId = new YouTubeGetWatchHistoryIdRequest(context).execute();
        }

        if (TextUtils.isEmpty(historyId)) {
            throw new IllegalStateException("Watch history id is empty!!!");
        }

        List<String> lastHistoryVideoIdes = new YouTubeGetPlaylistItemsRequest(context, historyId, 10, null).execute();
        List<VideoData> videos = new YouTubeGetVideosInfoRequest(lastHistoryVideoIdes, context, null).execute();
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
