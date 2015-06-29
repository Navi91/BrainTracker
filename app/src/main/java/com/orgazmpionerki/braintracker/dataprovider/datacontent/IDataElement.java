package com.orgazmpionerki.braintracker.dataprovider.datacontent;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public interface IDataElement {
    public void setId(String id);

    public String getId();

    public void setName(String name);

    public String getName();

    public void setLength(int length);

    public int getLength();

    public void setBrainPoints(int points);

    public int getBrainPoints();
}
