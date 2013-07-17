package code.java.restya;


// a list of rest providers
public enum RestProviders {
 TWITTER(R.string.rest_provider_name,"https://api.twitter.com/oauth/authorize");
 
 int mProviderNameResId;

 String mServiceAuthUrl;
 
 public String getServiceAuthUrl() {
	return mServiceAuthUrl;
}


public int getProviderNameResId() {
	return mProviderNameResId;
}


RestProviders(int providerNameResId, String serviceAuthUrl){
	 
	 mProviderNameResId = providerNameResId;
	 mServiceAuthUrl = serviceAuthUrl;
 }
}
