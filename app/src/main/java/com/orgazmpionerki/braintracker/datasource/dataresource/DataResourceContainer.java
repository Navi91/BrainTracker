package com.orgazmpionerki.braintracker.datasource.dataresource;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy on 11.03.2015.
 */
public class DataResourceContainer {
    private Map<ResourceType, IDataResource> mDataResourceProviders;

    public DataResourceContainer(Context context) {
        mDataResourceProviders = new HashMap<ResourceType, IDataResource>();

        mDataResourceProviders.put(ResourceType.YOUTUBE, new YouTubeDataResource(context));
        mDataResourceProviders.put(ResourceType.FACEBOOK, new FacebookDataResource(context));
        mDataResourceProviders.put(ResourceType.TWITTER, new TwitterDataResource(context));
        mDataResourceProviders.put(ResourceType.VKONTAKTE, new VkontakteDataResource(context));
    }

    public Map<ResourceType, IDataResource> getDataResources() {
        return mDataResourceProviders;
    }
}
