package com.orgazmpionerki.braintracker.datasource.dataresource;

import com.orgazmpionerki.braintracker.datasource.updateresponse.IUpdateResponse;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public interface IDataResource {
    public IUpdateResponse loadData(long startTimeInterval);

    public boolean isEnabled();

    public DataResourceType getType();
}
