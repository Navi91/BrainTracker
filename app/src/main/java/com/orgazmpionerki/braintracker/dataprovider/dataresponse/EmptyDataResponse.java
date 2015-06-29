package com.orgazmpionerki.braintracker.dataprovider.dataresponse;

import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;

import java.util.ArrayList;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class EmptyDataResponse extends DataResponse {
    public EmptyDataResponse() {
        super(new ArrayList<IDataElement>());
    }
}
