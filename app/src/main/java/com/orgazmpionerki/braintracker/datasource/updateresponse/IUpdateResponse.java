package com.orgazmpionerki.braintracker.datasource.updateresponse;

import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;

import java.util.List;

/**
 * Created by Dmitriy on 24.03.2015.
 */
public interface IUpdateResponse {
    public boolean isSuccess();

    public List<IDataElement> getElements();
}
