package com.orgazmpionerki.braintracker.service.controllers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.orgazmpionerki.braintracker.service.BrainTrackerService;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class BrainTrackerServiceController extends BaseServiceController {

    public BrainTrackerServiceController() {
        setServiceClass(BrainTrackerService.class);
    }
}
