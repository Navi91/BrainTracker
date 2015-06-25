package com.braintracker.auth.tokens;

import org.json.JSONException;
import org.json.JSONObject;

import com.braintracker.util.Tracer;

/** Parses the JSON recieved from Google when we are swapping our auth code for access
 * 
 * Example: { "access_token" : "ya29.AHES6ZTtm7SuokEB-RGtbBty9IIlNiP9-eNMMQKtXdMP3sfjL1Fc", "token_type" : "Bearer", "expires_in" : 3600, "refresh_token" : "1/HKSmLFXzqP0leUihZp2xUt3-5wkU7Gmu2Os_eBnzw74" } */
public class TokenParser {

	public TokenParser() {}

	public static Tokens parse(JSONObject jsonObject) throws JSONException {
		Tracer.debug("PARSE TOKENS " + jsonObject.toString());
		String accessToken = jsonObject.getString("access_token");
		String tokenType = jsonObject.getString("token_type");
		int expiresIn = jsonObject.getInt("expires_in");
		String refreshToken = null;
		if (jsonObject.has("refresh_token")) {
			refreshToken = jsonObject.getString("refresh_token");
		}

		Tokens tokens = new Tokens(accessToken, refreshToken, expiresIn, tokenType);
		return tokens;
	}
}