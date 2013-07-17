package code.java.restya.app.ui;

import code.java.restya.core.ErrorCodes;

/**
 * this handler is used to communicate with the oauth webview
 *
 */
public interface OauthCallBackUrlHandler {

	public void onSuccess(String param);
	public void onFail(ErrorCodes error);
	
}
