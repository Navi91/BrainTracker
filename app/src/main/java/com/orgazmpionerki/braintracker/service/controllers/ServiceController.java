package com.orgazmpionerki.braintracker.service.controllers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class ServiceController implements IBrainServiceController {
    private Class serviceClass = null;

    public ServiceController(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public void startService(Context context) {
        context.startService(new Intent(context, serviceClass));
    }

    @Override
    public void stopService(Context context) {
        if (isServiceRunning(context)) {
            context.stopService(new Intent(context, serviceClass));
        }
    }

    @Override
    public boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
