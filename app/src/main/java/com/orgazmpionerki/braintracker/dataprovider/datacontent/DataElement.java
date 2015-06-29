package com.orgazmpionerki.braintracker.dataprovider.datacontent;

/**
 * Created by Dmitriy on 29.03.2015.
 */
public class DataElement implements IDataElement {
    private String mName;
    private int mLength;
    private int mPoints;
    private String mId;

    @Override
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setLength(int length) {
        mLength = length;
    }

    @Override
    public int getLength() {
        return mLength;
    }

    @Override
    public void setBrainPoints(int points) {
        mPoints = points;
    }

    @Override
    public int getBrainPoints() {
        return mPoints;
    }

    @Override
    public String toString() {
        return "Element { " + "Name = " + mName + " Length = " + mLength + " Points = " + mPoints + " Id = " + mId + "}";
    }
}
