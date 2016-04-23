package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class YouTubeGetWatchHistoryIdRequest extends YouTubeRequest<String> {

    public YouTubeGetWatchHistoryIdRequest(Context context) {
        super(context);
    }

    public YouTubeGetWatchHistoryIdRequest(Context context, RequestTaskCallback<String> callback) {
        super(context, callback);
    }

    @Override
    public String doRequest() throws Exception {
        YouTube.Channels.List list = getYouTube().channels().list("contentDetails").setMine(true).setOauthToken(getToken());
        ChannelListResponse response = list.execute();

        return response.getItems().get(0).getContentDetails().getRelatedPlaylists().getWatchHistory();
    }
}
