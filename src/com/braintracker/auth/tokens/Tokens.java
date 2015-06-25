package com.braintracker.auth.tokens;

import java.io.Serializable;

/** Wrapper for holding the access and other tokens * */
public class Tokens implements Serializable {
	private static final long serialVersionUID = 4115408042757863939L;

	private final String mAccessToken;
	private final String mRefreshToken;
	private final int mExpiresIn;
	private final String mTokenType;

	public Tokens(String accessToken, String refreshToken, int expiresIn, String tokenType) {
		mAccessToken = accessToken;
		mRefreshToken = refreshToken;
		mExpiresIn = expiresIn;
		mTokenType = tokenType;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public String getRefreshToken() {
		return mRefreshToken;
	}

	public int getExpiresIn() {
		return mExpiresIn;
	}

	public String getTokenType() {
		return mTokenType;
	};
}