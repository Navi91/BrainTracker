package com.orgazmpionerki.braintracker.datasource.updateresponse;

import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;

import java.util.List;

/**
 * Created by Dmitriy on 28.03.2015.
 */
public class UpdateResponse implements IUpdateResponse {
    private boolean mSuccess = false;
    private List<IDataElement> mElements;

    public UpdateResponse() {
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    @Override
    public boolean isSuccess() {
        return mSuccess;
    }

    public void setElements(List<IDataElement> elements) {
        mElements = elements;
    }

    @Override
    public List<IDataElement> getElements() {
        return mElements;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Update Response Elements: \n");

        if (mElements != null && mElements.size() != 0) {
            for (IDataElement element : mElements) {
                builder.append(element.toString() + "\n");
            }
        } else {
            builder.append("empty");
        }

        return builder.toString();
    }
}
