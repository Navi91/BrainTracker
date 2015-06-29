package com.orgazmpionerki.braintracker.datasource.updaterequest;

import com.orgazmpionerki.braintracker.datasource.dataresource.IDataResource;

/**
 * Created by Dmitriy on 11.03.2015.
 */
public interface IUpdateRequest {
    public boolean execute();

    public void onUpdatingDone();

    public boolean isSuccess();

    public IDataResource getResource();
}
