package com.orgazmpionerki.braintracker.dataprovider;

/**
 * Created by Dmitriy on 20.04.2016.
 */
public class VideoData {
    public String resourceId;
    public String id;
    public String category;
    public String title;
    public String length;
    public int points;

    public static VideoData create(String resourceId, String id, String category, String title, String length, int points) {
        VideoData data = new VideoData();

        data.resourceId = resourceId;
        data.id = id;
        data.category = category;
        data.title = title;
        data.length = length;
        data.points = points;

        return data;
    }

    @Override
    public String toString() {
        return String.format("Video { resource: %s id: %s category: %s title: %s length: %s points: %s }", resourceId, id, category, title, length, points);
    }
}
