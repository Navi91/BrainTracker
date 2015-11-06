package com.orgazmpionerki.braintracker;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.orgazmpionerki.braintracker.util.Tracer;

/**
 * Created by Dmitriy on 15.09.2015.
 */
public class MessageService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Tracer.debug("message_debug", "onMessageReceived");
        super.onMessageReceived(messageEvent);
    }
}
