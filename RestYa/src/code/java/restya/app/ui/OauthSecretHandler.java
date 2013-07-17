package code.java.restya.app.ui;

import code.java.restya.core.ErrorCodes;

public interface OauthSecretHandler {
	
	public void onSuccess();
	public void onFail(ErrorCodes error);

}
