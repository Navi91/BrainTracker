package com.orgazmpionerki.braintracker.auth.youtube;

import android.content.Context;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.auth.AuthTokenTask;
import com.orgazmpionerki.braintracker.auth.tokens.TokenParser;
import com.orgazmpionerki.braintracker.auth.tokens.TokenRetrievedListener;
import com.orgazmpionerki.braintracker.auth.tokens.Tokens;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.util.StreamConverter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * This task exchanges the authorization code for an Access Token and a Refresh Token, when complete it will Log these out to the console, in a development environment you would save them to the shared preferences so you can use them for other calls to the YouTube API
 */
public class YoutubeHttpTokensTask implements AuthTokenTask {
    // The authcode you obtained when the user granted your app access to the
    // YouTube account
    private final String mAuthCode;
    private Context mContext;
    private TokenRetrievedListener mRetrievedListener;

    public YoutubeHttpTokensTask(Context context, String authCode) {
        mContext = context;
        mAuthCode = authCode;
    }

    public YoutubeHttpTokensTask(Context context, String authCode, TokenRetrievedListener retrievedListener) {
        mContext = context;
        mAuthCode = authCode;
        mRetrievedListener = retrievedListener;
    }

    @Override
    public boolean refresh() {
        try {
            HttpResponse response;
            if (Preferences.getRefreshKey(mContext) != null) {
                response = requestForRefreshYouTubeAccessTokens();
            } else {
                response = requestYouTubeAccessTokens();
            }
            Tokens tokens = parseYouTubeAccessTokens(response);
            updateKeys(tokens);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void run() {
        try {
            HttpResponse response;
            if (Preferences.getRefreshKey(mContext) != null) {
                response = requestForRefreshYouTubeAccessTokens();
            } else {
                response = requestYouTubeAccessTokens();
            }
            Tokens tokens = parseYouTubeAccessTokens(response);
            updateKeys(tokens);

            if (mRetrievedListener != null) {
                mRetrievedListener.onTokensRetrieved(tokens);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateKeys(Tokens tokens) {
        Preferences.putAccessKey(mContext, tokens.getAccessToken());
        if (tokens.getRefreshToken() != null) {
            Preferences.putRefreshKey(mContext, tokens.getRefreshToken());
        }
    }

    /**
     * Fires off the request for an access token to the Google servers
     *
     * @return the response which should contain JSON holding the access token
     */
    private HttpResponse requestYouTubeAccessTokens() throws IOException, ClientProtocolException {
        HttpPost post = createTokenRetrievalPost();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        return response;
    }

    private HttpResponse requestForRefreshYouTubeAccessTokens() throws IOException, ClientProtocolException {
        HttpPost post = createTokenRefreshAccessKeyPost();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);

        Tracer.debug("RESPONSE REFRESH KEY " + response.toString());

        return response;
    }

    /**
     * To gain an access token we have to send google our auth code and client credential's, these are passed into this task found in your API console https://code.google.com/apis/console respectively
     *
     * @return returns the Post request that we can then execute
     */
    private HttpPost createTokenRetrievalPost() throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(Constants.TOKENS_URL);
        post.setHeader("content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("code", mAuthCode));
        nameValuePair.add(new BasicNameValuePair("client_id", Constants.CLIENT_ID));
        nameValuePair.add(new BasicNameValuePair("redirect_uri", Constants.CALLBACK_URL));
        nameValuePair.add(new BasicNameValuePair("grant_type", "authorization_code"));
        post.setEntity(new UrlEncodedFormEntity(nameValuePair));

        return post;
    }

    private HttpPost createTokenRefreshAccessKeyPost() throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(Constants.TOKENS_URL);
        post.setHeader("content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("client_id", Constants.CLIENT_ID));
        nameValuePair.add(new BasicNameValuePair("refresh_token", Preferences.getRefreshKey(mContext)));
        nameValuePair.add(new BasicNameValuePair("grant_type", "refresh_token"));
        post.setEntity(new UrlEncodedFormEntity(nameValuePair));

        Tracer.debug("REFRESH POST " + post.toString());
        return post;
    }

    /**
     * @param response The response from the YouTUbe post request we just made
     * @return Our TokenParser so we can read the fields off it (just for logging)
     */
    private Tokens parseYouTubeAccessTokens(HttpResponse response) throws JSONException, IOException {
        JSONObject jsonObject = StreamConverter.convertStreamToJsonObject(response.getEntity().getContent());

        return TokenParser.parse(jsonObject);
    }
}