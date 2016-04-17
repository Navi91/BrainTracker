package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;

import java.util.List;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class YouTubeGetPlaylistItemsRequest extends YouTubeRequest<List<String>> {

    public YouTubeGetPlaylistItemsRequest(Context context, RequestTaskCallback<List<String>> callback) {
        super(context, callback);
    }

    public YouTubeGetPlaylistItemsRequest(Context context, HandleErrorRequestCallback<List<String>> errorRequestCallback) {
        super(context, errorRequestCallback);
    }


}
