package com.orgazmpionerki.braintracker.datasource.dataresource;

import android.content.Context;

import com.orgazmpionerki.braintracker.datasource.updateresponse.IUpdateResponse;

/**
 * Created by Dmitriy on 28.03.2015.
 */
public abstract class DataResource implements IDataResource {
    private Context mContext;

    public DataResource(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }
}
