package com.orgazmpionerki.braintracker.datasource;

import android.content.Context;
import android.os.Handler;

import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.datasource.dataresource.DataResourceContainer;
import com.orgazmpionerki.braintracker.datasource.dataresource.IDataResource;
import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequest;
import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequestListener;
import com.orgazmpionerki.braintracker.datasource.updaterequest.UpdateRequest;
import com.orgazmpionerki.braintracker.util.TimeManager;
import com.orgazmpionerki.braintracker.util.Tracer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 25.03.2015.
 */
public class UpdateDataManager implements IUpdateRequestListener {
    private final static String DEBUG_TAG = "update_data_manager_debug";

    private UpdateDataRequestExecutor mRequestExecutor;
    private DataResourceContainer mContainer;
    private Context mContext;
    private BrainTrackerDatabase mDatabase;
    private Handler mHandler;
    private int mMinUpdateDelay = 20; // in seconds
    private long mUpdatePeriod = 21 * 1000; // in milliseconds
    private boolean mIsRunning = false;
    private List<IUpdateRequestListener> mListeners;
    private UpdateTask mCurrentUpdateTask;

    private static UpdateDataManager mInstance;

    public static UpdateDataManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UpdateDataManager(context);
        }

        return mInstance;
    }

    private UpdateDataManager(Context context) {
        mContext = context;
        mContainer = new DataResourceContainer(context);
        mRequestExecutor = new UpdateDataRequestExecutor();
        mHandler = new Handler();
        mListeners = new ArrayList<IUpdateRequestListener>();
    }

    // start observing resources
    public void start() {
        if (mIsRunning) {
            return;
        }

        mIsRunning = true;
        mDatabase = new BrainTrackerDatabase(mContext);
        mDatabase.open();
        stopUpdateTask();
        startNewUpdateTask();
    }

    // stop observing resources
    public void stop() {
        stopUpdateTask();
        mDatabase.close();
        mIsRunning = false;
    }

    public void addListener(IUpdateRequestListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(IUpdateRequestListener listener) {
        mListeners.remove(listener);
    }

    private void updateResource(IDataResource resource) {
        // check, that db not closing from another thread (method onDestroy in BrainTrackerService example)
        if (mDatabase == null || !mDatabase.isOpen()) {
            return;
        }

        // get last updating time of cuurent resource
        int lastUpdateTime = mDatabase.getResourceLastUpdateTime(resource.getType());
        Tracer.debug(DEBUG_TAG, "updateResource last = " + lastUpdateTime + " current " + TimeManager.getCurrentTimeInSeconds());

        // not updating if recently was it
        if (TimeManager.getCurrentTimeInSeconds() - lastUpdateTime < mMinUpdateDelay) {
            return;
        }

        // create update request to get information about new data
        UpdateRequest request = new UpdateRequest(mContext, resource);
        request.setListener(this);

        // add request to executor
        mRequestExecutor.addRequest(request);
    }

    @Override
    public void onUpdateDone(IUpdateRequest request) {
        // if request have new data from resource
        if (request.isSuccess() && mDatabase != null && mDatabase.isOpen()) {
            // change of last success update
            mDatabase.updateResourceLastUpdateTime(request.getResource().getType());
            // notify listeners about new data
            notifyListenersUpdateDone(request);
        }
    }

    // notify all listeners about successing request
    private void notifyListenersUpdateDone(IUpdateRequest request) {
        for (IUpdateRequestListener listener : mListeners) {
            if (listener != null) {
                listener.onUpdateDone(request);
            }
        }
    }

    private void stopUpdateTask() {
        // stop runnable
        if (mCurrentUpdateTask != null) {
            mCurrentUpdateTask.kill();
        }
    }

    private void startNewUpdateTask() {
        // if previous task not stopped we lose memory
        if (mCurrentUpdateTask != null && !mCurrentUpdateTask.isKilled()) {
            throw new UnsupportedOperationException("Previous update task not stopped!!!");
        }

        // create new task and run it
        mCurrentUpdateTask = new UpdateTask();
        mCurrentUpdateTask.run();
    }

    private class UpdateTask implements Runnable {

        private boolean mKilled = false;

        public UpdateTask() {
        }

        @Override
        public void run() {
            // nothing doing if task killed
            if (mKilled) {
                return;
            }

            // update all enabled resources
            for (IDataResource resource : mContainer.getDataResources().values()) {
                if (resource.isEnabled()) {
                    updateResource(resource);
                }
            }

            // repeat this task after update period
            mHandler.postDelayed(this, mUpdatePeriod);
        }

        // stop this runnable
        public void kill() {
            mKilled = true;
        }

        public boolean isKilled() {
            return mKilled;
        }
    }
}
