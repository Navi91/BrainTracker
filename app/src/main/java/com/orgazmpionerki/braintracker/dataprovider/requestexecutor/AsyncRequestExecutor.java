package com.orgazmpionerki.braintracker.dataprovider.requestexecutor;

import com.orgazmpionerki.braintracker.dataprovider.datarequest.IDataRequest;

import java.util.List;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class AsyncRequestExecutor implements IRequestExecutor {
    private List<IDataRequest> mRequests;

    private static AsyncRequestExecutor mInstance;

    static {
        mInstance = new AsyncRequestExecutor();
    }

    public static AsyncRequestExecutor getInstance() {
        return mInstance;
    }

    private AsyncRequestExecutor() {
    }

    @Override
    public void addRequest(IDataRequest request) {

    }
}
