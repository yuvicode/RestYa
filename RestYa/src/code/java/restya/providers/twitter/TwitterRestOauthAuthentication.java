package code.java.restya.providers.twitter;

import code.java.restya.RestListDef;
import code.java.restya.app.ui.OauthAuthorizeHandler;
import code.java.restya.core.RestAuthenticationProvider;
import code.java.restya.core.RestLocalOauthPersistenceProvider;

import com.google.inject.Inject;

public class TwitterRestOauthAuthentication implements RestAuthenticationProvider{

RestLocalOauthPersistenceProvider mPersist;



	
	@Inject 
	TwitterRestOauthAuthentication(RestLocalOauthPersistenceProvider persist){
		mPersist = persist;

	}
	
	
	@Override
	public void login(final RestListDef restyaTwitterDemoServiceDef, OauthAuthorizeHandler handler) {

		TwitterAsyncOAuthTokenRequest twitterAsyncOAuthTokenRequest = new TwitterAsyncOAuthTokenRequest(restyaTwitterDemoServiceDef, mPersist,  handler);
		twitterAsyncOAuthTokenRequest.execute();
		
	}

	@Override
	public void forget(RestListDef restDef) {
		mPersist.clear(restDef);
	}

	@Override
	public boolean isLoggedIn(RestListDef restDef) {
		if (mPersist.isLogin(restDef))
			return true;
		else
			return false;
	}

}
