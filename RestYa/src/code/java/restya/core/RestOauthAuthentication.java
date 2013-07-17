package code.java.restya.core;

import code.java.restya.RestListDef;
import code.java.restya.app.ui.OauthAuthorizeHandler;

import com.google.inject.Inject;

/**
 * General OAuth implementation for rest services.
 * TODO: implement with Android SDK tools 
 *
 */
public class RestOauthAuthentication implements RestAuthenticationProvider {
	
	RestLocalOauthPersistenceProvider mPersist;
	
	@Inject 
	RestOauthAuthentication(RestLocalOauthPersistenceProvider persist){
		mPersist = persist;
	}
	
	



	@Override
	public void forget(RestListDef restDef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLoggedIn(RestListDef restDef) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public void login(RestListDef restDef, OauthAuthorizeHandler authorization) {
		// TODO Auto-generated method stub
		
	}





}
