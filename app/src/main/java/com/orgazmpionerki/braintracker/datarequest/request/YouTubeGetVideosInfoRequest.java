package com.orgazmpionerki.braintracker.datarequest.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;
import com.orgazmpionerki.braintracker.dataprovider.data.ResourceType;
import com.orgazmpionerki.braintracker.util.BrainTrackerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 19.04.2016.
 */
public class YouTubeGetVideosInfoRequest extends YouTubeRequest<List<VideoData>> {
    private List<String> ides;

    public YouTubeGetVideosInfoRequest(List<String> ides, Context context, RequestTaskCallback<List<VideoData>> callback) {
        super(context, callback);
        init(ides);
    }

    public YouTubeGetVideosInfoRequest(List<String> ides, Context context, HandleErrorRequestCallback<List<VideoData>> errorRequestCallback) {
        super(context, errorRequestCallback);
        init(ides);
    }

    private void init(List<String> ides) {
        this.ides = ides;
    }

    @Override
    public List<VideoData> doRequest() throws Exception {
        String id = createRequestId(ides);

        YouTube.Videos.List requestList = getYouTube().videos().list("contentDetails,snippet").setId(id).setOauthToken(getToken());

        VideoListResponse response = requestList.execute();

        List<VideoData> result = new ArrayList<>();

        for (Video video : response.getItems()) {
            result.add(createFrom(video));
        }

        return result;
    }

    private VideoData createFrom(Video video) {
        String id = video.getId();
        String category = video.getSnippet().getCategoryId();
        String title = video.getSnippet().getTitle();
        String length = video.getContentDetails().getDuration();
        int points = BrainTrackerUtil.getVideoPoints(category, BrainTrackerUtil.getVideoLengthInMinutes(length));

        return VideoData.create(ResourceType.YOUTUBE.getId(), id, category, title, length, points);
    }

    private String createRequestId(List<String> ides) {
        String result = "";

        for (String id : ides) {
            result += "," + id;
        }

        return result;
    }
}