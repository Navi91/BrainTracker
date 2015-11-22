package com.orgazmpionerki.braintracker.wear;

import android.content.Context;
import android.os.Bundle;

import com.dkrasnov.util_android_lib.Tracer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.orgazmpionerki.braintracker.handledwearcontract.WearContract;

import java.util.Set;

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

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        for (final Node node : getConnectedNodesResult.getNodes()) {
                            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                                    WearContract.DATA_PATH_POINT_CHANGED, new byte[0]).setResultCallback(
                                    getSendMessageResultCallback());
                        }
                    }
                });
    }

    private ResultCallback<MessageApi.SendMessageResult> getSendMessageResultCallback() {
        return new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                if (!sendMessageResult.getStatus().isSuccess()) {
                    Tracer.debug(DEBUG_TAG, "Failed to connect to Google Api Client with status "
                            + sendMessageResult.getStatus());
                }
            }
        };
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

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }
}
