package code.java.restya.core;

import code.java.restya.RestListDef;

/**
 * Persistence for Oauth parameters
 *
 */
public interface RestLocalOauthPersistenceProvider {

	public void setOauthToken(RestListDef service,String token);
	public void setOauthSecret(RestListDef service,String secret);
	public String getOauthToken(RestListDef service);
	public String getOauthSecret(RestListDef service);
	public boolean isLogin(RestListDef service);
	public void clear(RestListDef service);
	
}
