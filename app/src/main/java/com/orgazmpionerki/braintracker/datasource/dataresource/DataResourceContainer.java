package com.orgazmpionerki.braintracker.datasource.dataresource;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy on 11.03.2015.
 */
public class DataResourceContainer {
    private Map<DataResourceType, IDataResource> mDataResourceProviders;

    public DataResourceContainer(Context context) {
        mDataResourceProviders = new HashMap<DataResourceType, IDataResource>();

        mDataResourceProviders.put(DataResourceType.YOUTUBE, new YouTubeDataResource(context));
        mDataResourceProviders.put(DataResourceType.FACEBOOK, new FacebookDataResource(context));
        mDataResourceProviders.put(DataResourceType.TWITTER, new TwitterDataResource(context));
        mDataResourceProviders.put(DataResourceType.VKONTAKTE, new VkontakteDataResource(context));
    }

    public Map<DataResourceType, IDataResource> getDataResources() {
        return mDataResourceProviders;
    }
}
