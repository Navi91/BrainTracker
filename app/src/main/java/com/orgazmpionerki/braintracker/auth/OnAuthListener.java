package com.orgazmpionerki.braintracker.auth;

public interface OnAuthListener {
	void onAuthorized(String authCode);

	void onRefused();
}