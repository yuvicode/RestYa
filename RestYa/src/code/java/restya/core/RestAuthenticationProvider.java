package code.java.restya.core;

import code.java.restya.RestListDef;

/**
 *Authentication for rest services
 *
 */
public interface RestAuthenticationProvider {
	
	public boolean login(RestListDef restDef);
	
	public boolean logOut(RestListDef restDef);
	
	public boolean isLoggedIn(RestListDef restDef);

}
