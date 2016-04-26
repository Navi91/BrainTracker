package com.orgazmpionerki.braintracker.datasource.updaterequest;

import android.content.Context;
import android.os.Bundle;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabaseImpl;
import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;
import com.orgazmpionerki.braintracker.datasource.dataresource.IDataResource;
import com.orgazmpionerki.braintracker.datasource.updateresponse.IUpdateResponse;

/**
 * Created by Dmitriy on 11.03.2015.
 */
public class UpdateRequest implements IUpdateRequest {
    private final static String DEBUG_TAG = "update_request_debug";

    public static final String BUNDLE_BEFORE_POINTS = "com.orgazmpionerki.braintracker.datasource.updaterequest.bundle_before_points";

    private IDataResource mResource;
    private Context mContext;
    private IUpdateRequestListener mListener;
    private boolean mResult = false;
    private Bundle mInfoBundle;

    public UpdateRequest(Context context, IDataResource resource) {
        mContext = context;
        mResource = resource;
        mInfoBundle = new Bundle();
    }

    public void setListener(IUpdateRequestListener listener) {
        mListener = listener;
    }

    @Override
    public boolean execute() {
        if (mResource == null || !mResource.isEnabled()) {
            return false;
        }

        IUpdateResponse response = mResource.loadData(getUpdateStartTime(mResource));

        if (response == null || !response.isSuccess()) {
            Tracer.debug(DEBUG_TAG, "response is  empty");
            return false;
        }

        Tracer.debug(DEBUG_TAG, "response: " + response.toString());

        if (!writeDataToDatabase(response)) {
            return false;
        }

        mResult = true;
        return true;
    }

    @Override
    public void onUpdatingDone() {
        if (mListener != null) {
            mListener.onUpdateDone(this);
        }
    }

    @Override
    public IDataResource getResource() {
        return mResource;
    }

    public boolean isSuccess() {
        return mResult;
    }

    private boolean writeDataToDatabase(IUpdateResponse response) {
        boolean writeResult = false;
        BrainTrackerDatabaseImpl database = new BrainTrackerDatabaseImpl(mContext);
        database.open();

        // get points count before request
        // for notifications
        mInfoBundle.putInt(BUNDLE_BEFORE_POINTS, database.getBrainPoints(1));

        for (IDataElement element : response.getElements()) {
            writeResult |= database.addVideoToWatchHistory(element);
        }

        database.close();
        return writeResult;
    }

    private long getUpdateStartTime(IDataResource resource) {
        return 0;
    }

    @Override
    public Bundle getInfo() {
        return mInfoBundle;
    }
}
