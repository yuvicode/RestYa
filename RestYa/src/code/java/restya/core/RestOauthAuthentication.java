package code.java.restya.core;

import code.java.restya.RestListDef;

import com.google.inject.Inject;

/**
 * OAuth implementation for rest services 
 *
 */
public class RestOauthAuthentication implements RestAuthenticationProvider {
	
	RestLocalOauthPersistenceProvider mPersist;
	
	@Inject 
	RestOauthAuthentication(RestLocalOauthPersistenceProvider persist){
		mPersist = persist;
	}
	
	

	@Override
	public boolean login(RestListDef restDef) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean logOut(RestListDef restDef) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn(RestListDef restDef) {
		// TODO Auto-generated method stub
		return false;
	}

}
