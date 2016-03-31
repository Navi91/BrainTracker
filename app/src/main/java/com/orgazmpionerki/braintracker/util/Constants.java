package com.orgazmpionerki.braintracker.util;

public class Constants {

	// Client ID from https://code.google.com/apis/console API Access
//	public static final String CLIENT_ID = "1083931175059-01ocers4lcvkuebpk55ei7q33683q0nv.apps.googleusercontent.com";
	public static final String CLIENT_ID = "1083931175059-n79a6h6h56u5sj25k20l8vuqqp0n7avc.apps.googleusercontent.com ";
	// public static final String CLIENT_ID = "162895405734-9a6u3q2bakje5h9egk27jarras4h45dp.apps.googleusercontent.com";
	// Callback URL from https://code.google.com/apis/console API Access
	public static final String CALLBACK_URL = "http://localhost";

	// Brain Tracker notification id
	public static final int NOTIFICATION_ID = 001;

	// Wifi receiver intent filter action
	public static final String WIFI_INTENT_FILTER_ACTION = "android.net.wifi.STATE_CHANGE";

	public static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth?" + "client_id=" + CLIENT_ID + "&" + "redirect_uri=" + CALLBACK_URL + "&" + "scope=https://www.googleapis.com/auth/youtube&" + "response_type=code&" + "access_type=offline";

	public static final String AUTH_CODE_PARAM = "?code=";
	// This is the url used to exchange your auth code for an access token
	public static final String TOKENS_URL = "https://accounts.google.com/o/oauth2/token";
	// This is base url for request watch hitory
	public static final String BASE_REQUEST_URL = "https://www.googleapis.com/youtube/v3";

	// Day in millis
	public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
}
