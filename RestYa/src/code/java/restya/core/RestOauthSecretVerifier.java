package code.java.restya.core;

import code.java.restya.app.ui.OauthSecretHandler;

public interface RestOauthSecretVerifier {

	public void verify( String token, OauthSecretHandler handler);
}
