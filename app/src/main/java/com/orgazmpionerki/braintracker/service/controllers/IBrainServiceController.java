package com.orgazmpionerki.braintracker.service.controllers;

import android.content.Context;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public interface IBrainServiceController {
    public void startService(Context context);

    public void stopService(Context context);

    public boolean isServiceRunning(Context context);
}
