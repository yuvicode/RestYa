package code.java.restya;


// a list of rest providers
public enum RestProviders {
 TWITTER(R.string.rest_provider_name);
 
 int mProviderNameResId;
	 
 
 public int getProviderNameResId() {
	return mProviderNameResId;
}


RestProviders(int providerNameResId){
	 
	 mProviderNameResId = providerNameResId;
 }
}
