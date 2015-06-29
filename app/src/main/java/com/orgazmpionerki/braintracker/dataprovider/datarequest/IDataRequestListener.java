package com.orgazmpionerki.braintracker.dataprovider.datarequest;

import com.orgazmpionerki.braintracker.dataprovider.dataresponse.IDataResponse;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public interface IDataRequestListener {
    public void onRequestExecuted(IDataResponse request);
}
