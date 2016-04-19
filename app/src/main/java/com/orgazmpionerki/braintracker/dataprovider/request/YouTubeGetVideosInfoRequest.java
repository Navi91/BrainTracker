package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 19.04.2016.
 */
public class YouTubeGetVideosInfoRequest extends YouTubeRequest<List<String>> {
    private List<String> ides;

    public YouTubeGetVideosInfoRequest(List<String> ides, Context context, RequestTaskCallback<List<String>> callback) {
        super(context, callback);
        init(ides);
    }

    public YouTubeGetVideosInfoRequest(List<String> ides, Context context, HandleErrorRequestCallback<List<String>> errorRequestCallback) {
        super(context, errorRequestCallback);
        init(ides);
    }

    private void init(List<String> ides) {
        this.ides = ides;
    }

    @Override
    public List<String> doRequest() throws Exception {
        String id = createRequestId(ides);

        YouTube.Videos.List requestList = getYouTube().videos().list("contentDetails,snippet").setId(id).setOauthToken(getToken());

        VideoListResponse response = requestList.execute();

        List<String> result = new ArrayList<>();

        for (Video video : response.getItems()) {
            result.add(video.getSnippet().getCategoryId());
        }

        return result;
    }

    private String createRequestId(List<String> ides) {
        String result = "";

        for (String id : ides) {
            result += "," + id;
        }

        return result;
    }
}