package code.java.restya.core;

import code.java.restya.RestListDef;
import code.java.restya.app.ui.OauthAuthorizeHandler;

/**
 *Authentication service for rest providers, based on oauth
 *
 */
public interface RestAuthenticationProvider {
	
	public void login(RestListDef restDef, OauthAuthorizeHandler authorization);
	
	public void forget(RestListDef restDef);
	
	public boolean isLoggedIn(RestListDef restDef);

}
