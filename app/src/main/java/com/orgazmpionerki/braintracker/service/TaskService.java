package com.orgazmpionerki.braintracker.service;

import android.app.IntentService;
import android.content.Intent;

import com.dkrasnov.util_android_lib.Tracer;
import com.dkrasnov.util_android_lib.taskexecutor.RequestExecutor;
import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTask;
import com.orgazmpionerki.braintracker.dataprovider.BrainTrackerDataProvider;
import com.orgazmpionerki.braintracker.dataprovider.BrainTrackerDataProviderImpl;
import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;
import com.orgazmpionerki.braintracker.datarequest.request.YouTubeGetNewVideoRequest;
import com.orgazmpionerki.braintracker.tracker.TrackEvent;
import com.orgazmpionerki.braintracker.tracker.TrackerListenerImpl;
import com.orgazmpionerki.braintracker.util.Preferences;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dmitriy on 08.05.2016.
 */
public class TaskService extends IntentService {
    public static final long DELAY_BETWEEN_REQUEST_MILLISECONDS = 20 * 1000L;

    private Calendar calendar;
    private RequestExecutor requestExecutor;
    private BrainTrackerDataProvider dataProvider;

    public TaskService() {
        super("task service");
        calendar = Calendar.getInstance();
        requestExecutor = new RequestExecutor();
        dataProvider = BrainTrackerDataProviderImpl.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Tracer.methodEnter("task_service");
        doIteration();
    }

    private void doIteration() {
        Tracer.methodEnter();
        long lastUpdateTime = Preferences.getLastUpdateTime(this);
        long diff = calendar.getTimeInMillis() - lastUpdateTime;
        boolean needRequest = diff > DELAY_BETWEEN_REQUEST_MILLISECONDS;

        if (needRequest) {
            doRequest();
        }
    }

    private void doRequest() {
        Tracer.methodEnter();
        Preferences.setLastUpdateTime(this, calendar.getTimeInMillis());

        RequestTask<List<VideoData>> currentUpdateRequest = new YouTubeGetNewVideoRequest(this, new HandleErrorRequestCallback<List<VideoData>>() {
            @Override
            public void onResult(RequestTask<List<VideoData>> requestTask) {
                if (Preferences.getServerRunning(TaskService.this)) {
                    TrackEvent trackEvent = createEvent(requestTask.getResult());
                    Intent intent = new Intent(TrackerListenerImpl.TRACK_EVENT_ACTION);
                    intent.putExtra(TrackerListenerImpl.TRACK_EVENT_EXTRA, trackEvent);

                    TaskService.this.sendBroadcast(intent);
                }

                Tracer.debug("Update result: \n " + requestTask.getResult().toString());
            }

            @Override
            public void onError(Exception e) {
                Tracer.debug("Exception when data is updating!!! " + e.getMessage());
            }
        });
        requestExecutor.asyncRequest(currentUpdateRequest);
    }

    private TrackEvent createEvent(List<VideoData> videos) {
        int changePoints = 0;
        for (VideoData videoData : videos) {
            changePoints = videoData.points;
        }

        TrackEvent trackEvent = new TrackEvent(changePoints);
        return trackEvent;
    }
}
