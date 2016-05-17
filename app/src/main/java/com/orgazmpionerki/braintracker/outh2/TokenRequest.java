package com.orgazmpionerki.braintracker.outh2;

import android.content.Context;

import com.dkrasnov.util_android_lib.taskexecutor.request.HandleErrorRequestCallback;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

/**
 * Created by Dmitriy on 05.03.2016.
 */
public class TokenRequest extends RequestTaskBase<String> {
    private Context context;
    private String email;
    private String scope;

    public TokenRequest(Context context, String email, String scope) {
        super();
        init(context, email, scope);
    }

    public TokenRequest(Context context, String email, String scope, RequestTaskCallback<String> callback) {
        super(callback);
        init(context, email, scope);
    }

    public TokenRequest(Context context, String email, String scope, HandleErrorRequestCallback<String> callback) {
        super(callback);
        init(context, email, scope);
    }

    private void init(Context context, String email, String scope) {
        this.context = context;
        this.email = email;
        this.scope = scope;
    }

    @Override
    public String doRequest() throws Exception {
        return GoogleAuthUtil.getToken(context, email, scope);
    }
}
