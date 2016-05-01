package com.orgazmpionerki.braintracker.util;

import com.orgazmpionerki.braintracker.dataprovider.data.VideoData;
import com.orgazmpionerki.braintracker.datarequest.request.YouTubeProvider;

/**
 * Created by Dmitriy on 01.05.2016.
 */
public class BrainTrackerUtil {
    public static final int MAX_VIDEO_POINTS = 30;

    public static int getVideoPoints(VideoData videoData) {
        return getVideoPoints(videoData.category, getVideoLengthInMinutes(videoData.length));
    }

    public static int getVideoPoints(String category, int length) {
        int points = length * YouTubeProvider.getCategoryValue(category);

        if (points > MAX_VIDEO_POINTS) {
            points = MAX_VIDEO_POINTS;
        } else if (points < -MAX_VIDEO_POINTS) {
            points = -MAX_VIDEO_POINTS;
        }

        return points;
    }

    public static int getVideoLengthInMinutes(String lengthString) {
        int length = 0;
        int hours = 0;
        int minutes = 0;

        try {
            lengthString = lengthString.substring(2, lengthString.length());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return length;
        }

        // get index of hours
        int hourIndex = lengthString.indexOf("H");

        if (hourIndex != -1) {
            // get count of hours in length_string
            hours = Integer.parseInt(lengthString.substring(0, hourIndex));
            length += 60 * hours;

            try {
                lengthString = lengthString.substring(hourIndex + 1, lengthString.length());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return length;
            }
        }

        // get minute index
        int minuteIndex = lengthString.indexOf("M");

        if (minuteIndex != -1) {
            // get count of minutes in length_string
            minutes = Integer.parseInt(lengthString.substring(0, minuteIndex));
        }

        length += minutes;

        return length;
    }
}
