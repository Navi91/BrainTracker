package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class YouTubeGetPlaylistItemsRequest extends YouTubeRequest<List<String>> {
    private String playlistId;
    private long requestCount;

    public YouTubeGetPlaylistItemsRequest(Context context, String playlistId, long requestCount, RequestTaskCallback<List<String>> callback) {
        super(context, callback);
        init(playlistId, requestCount);
    }

    public YouTubeGetPlaylistItemsRequest(Context context, String playlistId, long requestCount, HandleErrorRequestCallback<List<String>> errorRequestCallback) {
        super(context, errorRequestCallback);
        init(playlistId, requestCount);
    }

    private void init(String playlistId, long requestCount) {
        this.playlistId = playlistId;
        this.requestCount = requestCount;
    }

    @Override
    public List<String> doRequest() throws Exception {
        YouTube.PlaylistItems.List request = getYouTube().playlistItems().list("contentDetails").setMaxResults(requestCount).setPlaylistId(playlistId).setOauthToken(getToken());

        PlaylistItemListResponse response = request.execute();
        List<String> ides = new ArrayList<>();

        for (PlaylistItem playlistItem: response.getItems()) {
            ides.add(playlistItem.getContentDetails().getVideoId());
        }

        return ides;
    }
}
