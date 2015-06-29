package com.orgazmpionerki.braintracker.dataprovider.datarequest;

import com.orgazmpionerki.braintracker.dataprovider.dataresponse.EmptyDataResponse;
import com.orgazmpionerki.braintracker.dataprovider.dataresponse.IDataResponse;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public abstract class DataRequest implements  IDataRequest {
    private IDataRequestListener mListener;
    private IDataResponse mDataResponse;

    public DataRequest(IDataRequestListener listener) {
        mListener = listener;
    }

    public IDataResponse execute() {
        if (!prepareData()) {
            return new EmptyDataResponse();
        }

        loadData();

        return mDataResponse;
    }

    private boolean prepareData() {
        // TODO connect to data provider
        return true;
    }

    protected abstract void loadData();

    @Override
    public void notifyDataRequestListener() {
        if (mListener != null) {
            mListener.onRequestExecuted(mDataResponse);
        }
    }
}
