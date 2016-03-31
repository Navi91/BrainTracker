package com.orgazmpionerki.braintracker.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import com.braintracker.R;
import com.dkrasnov.util_android_lib.Tracer;
import com.dkrasnov.util_android_lib.taskexecutor.RequestExecutor;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTask;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskBase;
import com.dkrasnov.util_android_lib.taskexecutor.request.RequestTaskCallback;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.client.util.Lists;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.orgazmpionerki.braintracker.Auth;
import com.orgazmpionerki.braintracker.database.BrainTrackerDatabase;
import com.orgazmpionerki.braintracker.outh2.GoogleAuthToken;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.wear.WearController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by Dmitriy on 29.06.2015.
 */
public class TestFragment extends BaseFragment {
    public static final String TAG = "com.braintracker.fargment.test_fragment";

    private boolean mFlag = false;
    private WearController mWearController;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    public TestFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWearController = new WearController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.test_fragment, null, true);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        layout.findViewById(R.id.test_button).setOnClickListener(view -> {
            mFlag = !mFlag;
            testYoutube();
        });


        setRetainInstance(true);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWearController.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWearController.disconnect();
    }

    @Override
    public void updateContent(Bundle args) {
    }


    GoogleAccountCredential mCredential;

    private void testYoutube() {

        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(YouTubeScopes.YOUTUBE))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(Preferences.getAccountEmail(getActivity()));

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        youtube = new YouTube.Builder(transport, jsonFactory, mCredential)
                .setApplicationName("Google API Android Quickstart")
                .build();

        String playlistId = null;

        RequestTaskBase<Boolean> requestTaskBase = new RequestTaskBase<Boolean>() {
            @Override
            public Boolean doRequest() {
                try {
                    String token = GoogleAuthToken.getToken(getActivity());
                    Tracer.debug("test_trace", "new token " + token);
//                    token = GoogleAuthUtil.getToken(getActivity(), Preferences.getAccountEmail(getActivity()), "oauth2: " + YouTubeScopes.YOUTUBE);
//                    Tracer.debug("test_trace", "new token " + token);

                    YouTube.PlaylistItems.List list = youtube.playlistItems().list("snippet").setOauthToken(token);
                    Tracer.debug("test_trace", list.toString());

                    PlaylistItemListResponse response = list.execute();
                    Tracer.debug("test_trace", response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Tracer.debug("test_trace", "Error: " + e.getClass());
                    if (e instanceof UserRecoverableAuthIOException) {
                        Intent intent = ((UserRecoverableAuthIOException) e).getIntent();
                        getActivity().startActivityForResult(intent, 1002);
                    }
                }
                return true;
            }
        };
        RequestExecutor executor = new RequestExecutor();
        executor.asyncRequest(requestTaskBase);

    }

    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    private static final String VIDEO_ID = "SZj6rAYkYOg";


    private void testYouTubeApi() {
        List<String> scopes = Arrays.asList(new String[]{"https://www.googleapis.com/auth/youtube"});

        try {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "playlistupdates");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                    .setApplicationName("youtube-cmdline-playlistupdates-sample")
                    .build();

            // Create a new, private playlist in the authorized user's channel.
            String playlistId = insertPlaylist();

            // If a valid playlist was created, add a video to that playlist.
            insertPlaylistItem(playlistId, VIDEO_ID);

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

    }

    private static String insertPlaylist() throws IOException {

        // This code constructs the playlist resource that is being inserted.
        // It defines the playlist's title, description, and privacy status.
        PlaylistSnippet playlistSnippet = new PlaylistSnippet();
        playlistSnippet.setTitle("Test Playlist " + Calendar.getInstance().getTime());
        playlistSnippet.setDescription("A private playlist created with the YouTube API v3");
        PlaylistStatus playlistStatus = new PlaylistStatus();
        playlistStatus.setPrivacyStatus("private");

        Playlist youTubePlaylist = new Playlist();
        youTubePlaylist.setSnippet(playlistSnippet);
        youTubePlaylist.setStatus(playlistStatus);

        // Call the API to insert the new playlist. In the API call, the first
        // argument identifies the resource parts that the API response should
        // contain, and the second argument is the playlist being inserted.
        YouTube.Playlists.Insert playlistInsertCommand =
                youtube.playlists().insert("snippet,status", youTubePlaylist);
        Playlist playlistInserted = playlistInsertCommand.execute();

        // Print data from the API response and return the new playlist's
        // unique playlist ID.
        Tracer.debug("test_trace", "New Playlist name: " + playlistInserted.getSnippet().getTitle());
        Tracer.debug("test_trace", " - Privacy: " + playlistInserted.getStatus().getPrivacyStatus());
        Tracer.debug("test_trace", " - Description: " + playlistInserted.getSnippet().getDescription());
        Tracer.debug("test_trace", " - Posted: " + playlistInserted.getSnippet().getPublishedAt());
        Tracer.debug("test_trace", " - Channel: " + playlistInserted.getSnippet().getChannelId() + "\n");
        return playlistInserted.getId();

    }

    /**
     * Create a playlist item with the specified video ID and add it to the
     * specified playlist.
     *
     * @param playlistId assign to newly created playlistitem
     * @param videoId    YouTube video id to add to playlistitem
     */
    private static String insertPlaylistItem(String playlistId, String videoId) throws IOException {

        // Define a resourceId that identifies the video being added to the
        // playlist.
        ResourceId resourceId = new ResourceId();
        resourceId.setKind("youtube#video");
        resourceId.setVideoId(videoId);

        // Set fields included in the playlistItem resource's "snippet" part.
        PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
        playlistItemSnippet.setTitle("First video in the test playlist");
        playlistItemSnippet.setPlaylistId(playlistId);
        playlistItemSnippet.setResourceId(resourceId);

        // Create the playlistItem resource and set its snippet to the
        // object created above.
        PlaylistItem playlistItem = new PlaylistItem();
        playlistItem.setSnippet(playlistItemSnippet);

        // Call the API to add the playlist item to the specified playlist.
        // In the API call, the first argument identifies the resource parts
        // that the API response should contain, and the second argument is
        // the playlist item being inserted.
        YouTube.PlaylistItems.Insert playlistItemsInsertCommand =
                youtube.playlistItems().insert("snippet,contentDetails", playlistItem);
        PlaylistItem returnedPlaylistItem = playlistItemsInsertCommand.execute();

        // Print data from the API response and return the new playlist
        // item's unique playlistItem ID.

        Tracer.debug("test_trace", "New PlaylistItem name: " + returnedPlaylistItem.getSnippet().getTitle());
        Tracer.debug("test_trace", " - Video id: " + returnedPlaylistItem.getSnippet().getResourceId().getVideoId());
        Tracer.debug("test_trace", " - Posted: " + returnedPlaylistItem.getSnippet().getPublishedAt());
        Tracer.debug("test_trace", " - Channel: " + returnedPlaylistItem.getSnippet().getChannelId());
        return returnedPlaylistItem.getId();

    }

    private void testWearConnection() {
        BrainTrackerDatabase database = new BrainTrackerDatabase(getActivity());
        database.open();

        mWearController.notifyPointsChanged(database.getBrainPoints(1));

        database.close();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}