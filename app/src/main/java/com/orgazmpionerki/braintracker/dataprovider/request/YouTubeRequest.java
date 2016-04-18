package com.orgazmpionerki.braintracker.dataprovider.request;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.api.services.youtube.YouTube;
import com.orgazmpionerki.braintracker.outh2.GoogleAuthToken;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class YouTubeRequest<K> extends RequestTaskBase<K> {
    private String mAuthToken;
    private Context mContext;
    private YouTube mYouTube;

    public YouTubeRequest(Context context, RequestTaskCallback<K> callback) {
        super(callback);
        initYouTubeElements(context);
    }

    public YouTubeRequest(Context context, HandleErrorRequestCallback<K> errorRequestCallback) {
        super(errorRequestCallback);
        initYouTubeElements(context);
    }

    private void initYouTubeElements(Context context) {
        mContext = context;
        mAuthToken = GoogleAuthToken.getToken(context);
        mYouTube = YouTubeProvider.getInstance(context);
    }

    @Override
    public K execute() {
        return super.execute();
    }

    public Context getContext() {
        return mContext;
    }

    public String getToken() {
        return mAuthToken;
    }

    public YouTube getYouTube() {
        return mYouTube;
    }
}
