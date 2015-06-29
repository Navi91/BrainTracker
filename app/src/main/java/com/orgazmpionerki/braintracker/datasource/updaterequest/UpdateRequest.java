package com.orgazmpionerki.braintracker.datasource.updaterequest;

import android.content.Context;

import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;
import com.orgazmpionerki.braintracker.datasource.dataresource.IDataResource;
import com.orgazmpionerki.braintracker.datasource.updateresponse.IUpdateResponse;
import com.orgazmpionerki.braintracker.util.Tracer;

/**
 * Created by Dmitriy on 11.03.2015.
 */
public class UpdateRequest implements IUpdateRequest {
    private final static String DEBUG_TAG = "update_request_debug";

    private IDataResource mResource;
    private Context mContext;
    private IUpdateRequestListener mListener;
    private boolean mResult = false;

    public UpdateRequest(Context context, IDataResource resource) {
        mContext = context;
        mResource = resource;
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
        BrainTrackerDatabase database = new BrainTrackerDatabase(mContext);
        database.open();

        for (IDataElement element : response.getElements()) {
            writeResult |= database.addVideoToWatchHistory(element);
        }

        database.close();
        return writeResult;
    }

    private long getUpdateStartTime(IDataResource resource) {
        return 0;
    }
}
