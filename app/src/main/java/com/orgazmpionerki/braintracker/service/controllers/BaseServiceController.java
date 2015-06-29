package com.orgazmpionerki.braintracker.service.controllers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.orgazmpionerki.braintracker.service.BrainTrackerService;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class BaseServiceController implements IBrainServiceController {
    private Class mServiceClass = null;

    protected void setServiceClass(Class serviceClass) {
        mServiceClass = serviceClass;
    }

    @Override
    public void startService(Context context) {
        context.startService(new Intent(context, mServiceClass));
    }

    @Override
    public void stopService(Context context) {
        if (isServiceRunning(context)) {
            context.stopService(new Intent(context, mServiceClass));
        }
    }

    @Override
    public boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (mServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
