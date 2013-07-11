package code.java.restya;

import code.java.restya.core.RestMetods;

/**
 * List of rest services and their main attributes
 *
 */
public enum RestListDef {
	
TWITTER_SEARCH(RestProviders.TWITTER,"https://api.twitter.com/1.1/search/tweets.json",
		RestMetods.GET,R.string.rest_service_name_twitter_search,"6YJCS09S2ULtdvYWsXDg","oos1HBsFLcjh0lPqG3y8wuzucH3e91I8l3kUuZ8FBdA","https://sites.google.com/site/twitterdemopage/");

private String mOAuthConsumerKey;
private String mOauthConsumersecret;
private String mUri;
private RestMetods mMethod;
int mTitleResId;
private String mCallBackUrl;
RestProviders mRestProviders;


public RestProviders getRestProvider(){
	return mRestProviders;
}


public String getUri() {
	return mUri;
}



public int getTitleResId() {
	return mTitleResId;
}



public RestMetods getmMethod() {
	return mMethod;
}




public String getOAuthConsumerKey() {
	return mOAuthConsumerKey;
}

public String getOauthConsumerSecret() {
	return mOauthConsumersecret;
}

public String getCallBackUrl() {
	return mCallBackUrl;
}

RestListDef(RestProviders restProviders,String uri, RestMetods method, int titleResId, String oAuthConsumerkey, String oauthConsumersecret,String callBackUrl){
	
	mRestProviders =restProviders;
	mUri=uri;
	mMethod = method;
	mTitleResId =titleResId;
	mOAuthConsumerKey = oAuthConsumerkey;
	mOauthConsumersecret = oauthConsumersecret;
	mCallBackUrl = callBackUrl;
	}





}
