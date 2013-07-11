package code.java.restya.core;

import android.content.Context;
import code.java.restya.RestListDef;

import com.google.inject.Inject;

public class RestLocalOauthSharedPrefPersistence extends RestSharedPrefsPersistence implements RestLocalOauthPersistenceProvider{

	
	@Inject
	RestLocalOauthSharedPrefPersistence(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	@Override
	public void setOauthToken(RestListDef service, String token) {
		setProviderString(service,PREF_KEY_OAUTH_TOKEN, token);
		
	}
	@Override
	public void setOauthSecret(RestListDef service, String secret) {
		setProviderString(service,PREF_KEY_OAUTH_SECRET, secret);
		
	}
	@Override
	public String getOauthToken(RestListDef service) {
		// TODO Auto-generated method stub
		return getProviderString(service, PREF_KEY_OAUTH_TOKEN);
	}
	@Override
	public String getOauthSecret(RestListDef service) {
		// TODO Auto-generated method stub
		 return getProviderString(service, PREF_KEY_OAUTH_SECRET);
	}
	@Override
	public boolean isLogin(RestListDef service) {
		if(getOauthSecret(service) !=null  && getOauthToken(service) !=null )
			return true;
		return false;
	}
	@Override
	public void clear(RestListDef service) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
