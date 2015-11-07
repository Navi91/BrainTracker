package com.orgazmpionerki.braintracker.auth.youtube;

import android.accounts.AccountManager;
import android.content.Context;

import com.orgazmpionerki.braintracker.auth.AuthTokenTask;
import com.orgazmpionerki.braintracker.auth.tokens.TokenRetrievedListener;
import com.orgazmpionerki.braintracker.util.Preferences;

/**
 * Created by Dmitriy on 06.11.2015.
 */
public class YouTubeAccountManagerTokensTask implements AuthTokenTask {
    private Context mContext;
    private TokenRetrievedListener mRetrievedListener;

    public YouTubeAccountManagerTokensTask(Context context) {
        mContext = context;
    }

    public YouTubeAccountManagerTokensTask(Context context, TokenRetrievedListener listener) {
        mContext = context;
        mRetrievedListener = listener;
    }

    @Override
    public void run() {
        if (Preferences.getRefreshKey(mContext) != null) {

        } else {

        }
    }

    @Override
    public boolean refresh() {
        return false;
    }
}
