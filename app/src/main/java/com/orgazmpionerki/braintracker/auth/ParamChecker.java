package com.orgazmpionerki.braintracker.auth;

import com.orgazmpionerki.braintracker.util.Constants;

/** This class checks what paramater's our redirect url has informing our listener it also passes the auth code if access is granted.
 * 
 * Google Documentation: If the user granted access to your application, Google will have appended a code parameter to the redirect_uri. This value is a temporary authorization code that you can exchange for an access token. example : http://localhost/oauth2callback?code=4/ux5gNj-_mIu4DOD_gNZdjX9EtOFf
 * 
 * If the user refused to grant access to your application, Google will have included the access_denied error message in the hash fragment of the redirect_uri. example : http://localhost/oauth2callback#error=access_denied */
public class ParamChecker implements OnAuthCallbackListener {

	private final OnAuthListener mOnAuthListener;

	public ParamChecker(OnAuthListener listener) {
		this.mOnAuthListener = listener;
	}

	@Override
	public void onAuthCallback(String params) {
		if (params.contains("access_denied")) {
			// User said no
			mOnAuthListener.onRefused();
		} else {
			// User auth'd us
			String authCode = extractAuthCode(params);
			mOnAuthListener.onAuthorized(authCode);
		}
	}

	private String extractAuthCode(String params) {
		return params.substring(Constants.AUTH_CODE_PARAM.length() + 1);
	}
}
