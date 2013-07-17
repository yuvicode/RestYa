package code.java.restya.app.ui;

import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestOauthSecretVerifier;

/**
 * this handler is design to be performed by {@link code.java.restya.core.RestAuthenticationProvider} after the calling Async oAuth authorization url
 *
 */
public interface OauthAuthorizeHandler {

	public void onSuccess(String url, RestOauthSecretVerifier secretHandler);
	public void onFail(ErrorCodes error);
	
}