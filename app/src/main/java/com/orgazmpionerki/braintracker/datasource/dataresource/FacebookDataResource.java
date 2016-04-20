package com.orgazmpionerki.braintracker.datasource.dataresource;

import android.content.Context;

import com.orgazmpionerki.braintracker.datasource.updateresponse.IUpdateResponse;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class FacebookDataResource extends DataResource {
    private final static ResourceType TYPE = ResourceType.FACEBOOK;

    public FacebookDataResource(Context context) {
        super(context);
    }

    @Override
    public IUpdateResponse loadData(long startTimeInterval) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public ResourceType getType() {
        return TYPE;
    }
}
