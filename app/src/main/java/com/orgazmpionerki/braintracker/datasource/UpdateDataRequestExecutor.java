package com.orgazmpionerki.braintracker.datasource;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.orgazmpionerki.braintracker.datasource.updaterequest.IUpdateRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmitriy on 11.03.2015.
 */
public class UpdateDataRequestExecutor {
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private ThreadPoolExecutor mRequestThreadPoolExecutor;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            IUpdateRequest request = (IUpdateRequest) msg.obj;
            request.onUpdatingDone();
        }
    };

    public UpdateDataRequestExecutor() {
        mRequestThreadPoolExecutor = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,  new LinkedBlockingDeque<>());
    }

    public void addRequest(IUpdateRequest request) {
        mRequestThreadPoolExecutor.execute(new RequestUpdateTask(request));
    }

    private void handleRequestExecuteDone(IUpdateRequest request) {
        Message message = mHandler.obtainMessage(0, request);
        message.sendToTarget();
    }

    private class RequestUpdateTask implements Runnable {
        private IUpdateRequest mRequest;

        public RequestUpdateTask(IUpdateRequest request) {
            mRequest = request;
        }

        @Override
        public void run() {
            mRequest.execute();
            handleRequestExecuteDone(mRequest);
        }
    }
}
