package com.orgazmpionerki.braintracker.dataprovider.dataresponse;

import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;

import java.util.List;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class DataResponse implements IDataResponse {
    private List<IDataElement> mResults;

    public DataResponse(List<IDataElement> results) {
        mResults = results;
    }

    @Override
    public List<IDataElement> getResults() {
        return mResults;
    }
}
