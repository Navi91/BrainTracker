package com.orgazmpionerki.braintracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dmitriy on 08.05.2016.
 */
public class TaskReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.orgazmpionerki.braintracker.service.action";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent taskServiceIntent = new Intent(context, TaskService.class);
        taskServiceIntent.putExtra("foo", "bar");
        context.startService(taskServiceIntent);
    }
}
