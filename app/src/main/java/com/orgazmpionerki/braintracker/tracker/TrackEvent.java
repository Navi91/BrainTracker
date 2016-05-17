package com.orgazmpionerki.braintracker.tracker;

import java.io.Serializable;

/**
 * Created by Dmitriy on 17.05.2016.
 */
public class TrackEvent implements Serializable {
    public int changePoints;

    public TrackEvent(int changePoints) {
        this.changePoints = changePoints;
    }
}
