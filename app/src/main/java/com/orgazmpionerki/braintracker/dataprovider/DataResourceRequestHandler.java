package com.orgazmpionerki.braintracker.dataprovider;

import com.orgazmpionerki.braintracker.dataprovider.datarequest.IDataRequest;
import com.orgazmpionerki.braintracker.dataprovider.requestexecutor.AsyncRequestExecutor;
import com.orgazmpionerki.braintracker.dataprovider.requestexecutor.IRequestExecutor;
import com.orgazmpionerki.braintracker.dataprovider.requestexecutor.RequestExecutor;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class DataResourceRequestHandler {
    private IRequestExecutor mAsyncRequestExecutor;
    private IRequestExecutor mRequestExecutor;

    public DataResourceRequestHandler() {
        mAsyncRequestExecutor = AsyncRequestExecutor.getInstance();
        mRequestExecutor = new RequestExecutor();
    }

    public void asyncRequest(IDataRequest request) {
        mAsyncRequestExecutor.addRequest(request);
    }

    public void request(IDataRequest request) {
        mRequestExecutor.addRequest(request);
    }
}
