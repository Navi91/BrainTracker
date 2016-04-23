package com.orgazmpionerki.braintracker.dataprovider;

import android.content.Context;

import com.dkrasnov.util_android_lib.Tracer;
import com.dkrasnov.util_android_lib.taskexecutor.RequestExecutor;
import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTask;
import com.orgazmpionerki.braintracker.dataprovider.request.YouTubeGetNewVideoRequest;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dmitriy on 22.04.2016.
 */
public class YouTubeRequestDataServer implements RequestDataServer {
    private static final long DELAY_BETWEEN_REQUEST_MILLISECONDS = 20 * 1000L;

    private boolean running = false;
    private Timer timer;
    private TimerTask newRequestTimerTask;
    private RequestTask<List<VideoData>> currentUpdateRequest;
    private Context context;
    private Calendar calendar;
    private RequestExecutor requestExecutor;

    public YouTubeRequestDataServer(Context context) {
        this.context = context;
        calendar = Calendar.getInstance();
        requestExecutor = new RequestExecutor();
    }

    @Override
    public void start() {
        Tracer.methodEnter();
        running = true;

        doIteration();
    }

    @Override
    public void stop() {
        Tracer.methodEnter();
        running = false;
        stopIteration();
    }

    @Override
    public boolean running() {
        return running;
    }

    private void doIteration() {
        Tracer.methodEnter();
        long lastUpdateTime = Preferences.getLastUpdateTime(context);
        long diff = calendar.getTimeInMillis() - lastUpdateTime;
        boolean needRequest = diff > DELAY_BETWEEN_REQUEST_MILLISECONDS;

        if (needRequest) {
            doRequest();
            return;
        }

        startTimerForRequest(DELAY_BETWEEN_REQUEST_MILLISECONDS - diff);
    }

    private void doRequest() {
        Tracer.methodEnter();
        Preferences.setLastUpdateTime(context, calendar.getTimeInMillis());

       currentUpdateRequest = new YouTubeGetNewVideoRequest(context, new HandleErrorRequestCallback<List<VideoData>>() {
            @Override
            public void onResult(RequestTask<List<VideoData>> requestTask) {
                if (!requestTask.cancelled()) {
                    doIteration();
                }

                Tracer.debug("Update result: \n " + requestTask.getResult().toString());
            }

            @Override
            public void onError(Exception e) {
                Tracer.debug("Exception when data is updating!!! " + e.getMessage());
                doIteration();
            }
        });
        requestExecutor.asyncRequest(currentUpdateRequest);
    }

    private void startTimerForRequest(long delay) {
        Tracer.debug("startTimerForRequest " + delay);
        timer = new Timer();
        newRequestTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    doRequest();
                }
            }
        };

        timer.schedule(newRequestTimerTask, delay);
    }

    private void stopIteration() {
        stopCurrentRequest();
        stopTimerForRequest();
    }

    private void stopCurrentRequest() {
        if (currentUpdateRequest != null) {
            currentUpdateRequest.cancel();
        }
    }

    private void stopTimerForRequest() {
        if (newRequestTimerTask != null) {
            newRequestTimerTask.cancel();
        }

        if (timer != null) {
            timer.cancel();
        }
    }
}
