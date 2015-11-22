package com.orgazmpionerki.braintracker.datasource.dataresource;

import android.content.Context;

import com.dkrasnov.util_android_lib.Tracer;
import com.orgazmpionerki.braintracker.auth.youtube.YoutubeHttpTokensTask;
import com.orgazmpionerki.braintracker.notification.WifiController;
import com.orgazmpionerki.braintracker.dataprovider.datacontent.IDataElement;
import com.orgazmpionerki.braintracker.dataprovider.datacontent.YoutubeDataElement;
import com.orgazmpionerki.braintracker.datasource.updateresponse.IUpdateResponse;
import com.orgazmpionerki.braintracker.datasource.updateresponse.UpdateResponse;
import com.orgazmpionerki.braintracker.util.Constants;
import com.orgazmpionerki.braintracker.util.MetricsConverter;
import com.orgazmpionerki.braintracker.util.Preferences;
import com.orgazmpionerki.braintracker.util.StreamConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy on 10.03.2015.
 */
public class YouTubeDataResource extends DataResource {
    private final static String DEBUG_TAG = "youtube_data_resource_debug";
    private final static DataResourceType TYPE = DataResourceType.YOUTUBE;

    public YouTubeDataResource(Context context) {
        super(context);
    }

    @Override
    public IUpdateResponse loadData(long startTimeInterval) {
        UpdateResponse updateResponse = new UpdateResponse();
        updateResponse.setSuccess(false);
        List<IDataElement> elements = new ArrayList<IDataElement>();
        String accessKey = Preferences.getAccessKey(getContext());

        if (!WifiController.isWifiConnected(getContext())) {
            return updateResponse;
        }

        try {
            JSONObject playlistIdes = getPlayListIdes(accessKey);
            if (playlistIdes == null) {
                return updateResponse;
            }

            if (playlistIdes.has("error") && playlistIdes.getJSONObject("error").toString().contains("Invalid Credentials")) {
                if (refreshAccessKey(getContext())) {
                    return loadData(startTimeInterval);
                }

                return updateResponse;
            }

            JSONObject playlist = getPlaylist(playlistIdes, accessKey);

            if (playlist == null) {
                return updateResponse;
            }

            JSONArray videosInPlaylist = playlist.getJSONArray("items");
            int resultsCount = (int) Math.min(playlist.getJSONObject("pageInfo").getLong("resultsPerPage"), playlist.getJSONObject("pageInfo").getLong("totalResults"));

            for (int i = 0; i < resultsCount; i++) {
                IDataElement element = createDataElement(videosInPlaylist.getJSONObject(i), accessKey);
                if (element != null) {
                    elements.add(element);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            updateResponse.setSuccess(false);
            return updateResponse;
        }

        updateResponse.setSuccess(true);
        updateResponse.setElements(elements);
        return updateResponse;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public DataResourceType getType() {
        return TYPE;
    }

    private boolean refreshAccessKey(Context context) {
        return new YoutubeHttpTokensTask(context, Preferences.getAuthCode(context)).refresh();
    }

    private IDataElement createDataElement(JSONObject video, String accessKey) {
        YoutubeDataElement element = new YoutubeDataElement();

        try {
            String video_id = video.getJSONObject("contentDetails").getString("videoId");
            HttpResponse videoInfo = requestVideoInfo(video_id, accessKey);
            JSONObject videoObject = StreamConverter.convertStreamToJsonObject(videoInfo.getEntity().getContent());

            if (videoObject.getJSONObject("pageInfo").getInt("totalResults") == 0) {
                return null;
            }

            JSONObject videoProperties = videoObject.getJSONArray("items").getJSONObject(0);

            String id = videoProperties.getString("id");
            String category = videoProperties.getJSONObject("snippet").getString("categoryId");
            String title = videoProperties.getJSONObject("snippet").getString("title");
            String length = videoProperties.getJSONObject("contentDetails").getString("duration");
            int brain_points = MetricsConverter.getBrainPoints(getContext(), category, MetricsConverter.getVideoLengthInMinutes(length));

            element.setId(id);
            element.setCategory(category);
            element.setName(title);
            element.setLength(MetricsConverter.getVideoLengthInMinutes(length));
            element.setBrainPoints(brain_points);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return element;
    }

    private JSONObject getPlaylist(JSONObject playlistIdes, String accessKey) {
        JSONObject playlistObject;
        try {
            String historyPlaylistId = playlistIdes.getJSONArray("items").getJSONObject(0).getJSONObject("contentDetails").getJSONObject("relatedPlaylists").getString("watchHistory");

            HttpResponse playlistResponse = requestPlaylist(historyPlaylistId, accessKey);
            playlistObject = StreamConverter.convertHttpResponseToJsonObject(playlistResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return playlistObject;
    }

    private JSONObject getPlayListIdes(String accessKey) {
        JSONObject playlistIdes;
        try {
            playlistIdes = StreamConverter.convertHttpResponseToJsonObject(requestPlayListId(accessKey));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return playlistIdes;
    }

    private HttpResponse requestPlayListId(String oauth2) throws IOException {
        Tracer.methodEnter();
        HttpGet get = new HttpGet(Constants.BASE_REQUEST_URL + "/channels?mine=true&part=contentDetails&access_token=" + oauth2);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        return response;
    }

    private HttpResponse requestPlaylist(String playlistId, String oauth2) throws IOException {
        Tracer.methodEnter();
        HttpGet get = new HttpGet(Constants.BASE_REQUEST_URL + "/playlistItems?part=contentDetails&maxResults=1&playlistId=" + playlistId + "&access_token=" + oauth2);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        return response;
    }

    private HttpResponse requestVideoInfo(String videoId, String oauth2) throws IOException {
        Tracer.methodEnter();
        HttpGet get = new HttpGet(Constants.BASE_REQUEST_URL + "/videos?id=" + videoId + "&part=contentDetails,snippet&access_token=" + oauth2);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        return response;
    }
}
