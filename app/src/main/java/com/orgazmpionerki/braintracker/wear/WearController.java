package com.orgazmpionerki.braintracker.wear;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.orgazmpionerki.braintracker.handledwearcontract.WearContract;
import com.orgazmpionerki.braintracker.util.Tracer;

/**
 * Created by Dmitriy on 25.08.2015.
 */
public class WearController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String DEBUG_TAG = "wear_controller_debug";

    private GoogleApiClient mGoogleApiClient;
    private Context mContext;

    public WearController(Context context) {
        mContext = context;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    public void notifyPointsChanged(int points) {
        Tracer.debug(DEBUG_TAG, "notifyPointsChanged " + points);

        if (!mGoogleApiClient.isConnected()) {
            return;
        }

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(WearContract.DATA_PATH_POINT_CHANGED);
        putDataMapReq.getDataMap().putInt(WearContract.BUNDLE_POINTS, points);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Tracer.debug(DEBUG_TAG, "onConnected");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Tracer.debug(DEBUG_TAG, "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Tracer.debug(DEBUG_TAG, "onConnectionFailed " + connectionResult.getErrorCode());

    }
}
