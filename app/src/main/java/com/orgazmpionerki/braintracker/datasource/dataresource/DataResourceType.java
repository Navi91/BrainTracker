package com.orgazmpionerki.braintracker.datasource.dataresource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public enum DataResourceType {
    YOUTUBE("youtube"), FACEBOOK("facebook"), TWITTER("twitter"), VKONTAKTE("vkontakte");

    private String mId;

    private DataResourceType(String id) {
        mId = id;
    }

    public String getId() {
        return  mId;
    }

    public static List<DataResourceType> getTypes() {
        return Arrays.asList(values());
    }
}
