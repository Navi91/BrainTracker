package com.orgazmpionerki.braintracker.datasource.dataresource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public enum ResourceType {
    YOUTUBE("youtube"), FACEBOOK("facebook"), TWITTER("twitter"), VKONTAKTE("vkontakte");

    private String mId;

    private ResourceType(String id) {
        mId = id;
    }

    public String getId() {
        return  mId;
    }

    public static List<ResourceType> getTypes() {
        return Arrays.asList(values());
    }
}
