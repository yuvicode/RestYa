package code.java.restya.providers.twitter;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.util.Log;
import code.java.restya.RestListDef;
import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestAsyncRespnseHandler;
import code.java.restya.core.RestLocalOauthPersistenceProvider;

class SearchTwitter extends AsyncTask<String, String, String> {
	
	private int start;
	
	private int num;

	TwitterSearchResponse response;
	
	boolean hasError =false;
	
	RestListDef mServiceDef;
	
	RestLocalOauthPersistenceProvider mPersist;
	
	ErrorCodes error;
	
	
	
	RestAsyncRespnseHandler<TwitterRestSearchResponseItem, TwitterSearchResponse> mHandler;
	
	 SearchTwitter(RestListDef serviceDef, RestAsyncRespnseHandler<TwitterRestSearchResponseItem , TwitterSearchResponse> handler, RestLocalOauthPersistenceProvider persist, int start, int num){
	 mServiceDef = serviceDef;
	 mHandler = handler;
	 this.num = num;
	 this.start= start;
	 mPersist = persist;
 }
 
 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       
    }


    protected String doInBackground(String... args) {

        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(mServiceDef.getOAuthConsumerKey());
            builder.setOAuthConsumerSecret(mServiceDef.getOauthConsumerSecret());
             
            String access_token = mPersist.getOauthToken(mServiceDef);
            String access_token_secret = mPersist.getOauthSecret(mServiceDef);
             
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
             
            Query query = new Query(args[0]);
    		query.setCount(num);
    		query.setSinceId(start);
    		
    		QueryResult result = null;
    		
    		result = twitter.search(query);
    		
    		response = new TwitterSearchResponse(true, "",result.getTweets().size(), true);
    			if(result !=null)
    				for (twitter4j.Status status  :  result.getTweets()) {
    					
    					response.addItem(new TwitterRestSearchResponseItem(status.getText(),status.getCreatedAt(), Long.toString(status.getId())));
    					
    				}
    			
    		
    		} catch (TwitterException e) {
    			hasError = true;
    			error = ErrorCodes.SERVICE_UNAUTHORIZED;
    			Log.d(SearchTwitter.class.getSimpleName(), e.getErrorMessage());
    			
    		} catch (Exception e) {
    			Log.d(SearchTwitter.class.getSimpleName(), e.getMessage());

    			hasError = true;
    			error = ErrorCodes.ERR_UNKNOWN;
    		}
            
            
        return null;
    }


    protected void onPostExecute(String results) {

    	if(hasError == false && response!=null )
    		mHandler.onSuccess(response);
    	else
    		mHandler.onError(error);
    }

}
