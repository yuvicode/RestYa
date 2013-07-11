package code.java.restya.providers.twitter;


import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import code.java.restya.RestListDef;
import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestAsyncRespnseHandler;
import code.java.restya.core.RestLocalOauthPersistenceProvider;
import code.java.restya.core.RestSearchProvider;

import com.google.inject.Inject;

/**
 * Twitter implementation for search logic
 * 
 */
public class TwitterSimpleSearchRestProvider extends TwitterRestProvider
		implements
		RestSearchProvider<TwitterRestSearchResponseItem, TwitterSearchResponse> {

	
	private final String tag = TwitterSimpleSearchRestProvider.class.getSimpleName();
	
	private Twitter twitter;
	
	RestAsyncRespnseHandler<TwitterRestSearchResponseItem, TwitterSearchResponse> mHandler;
	RestLocalOauthPersistenceProvider mPersist;
	@Inject
	public TwitterSimpleSearchRestProvider(Context ctx, RestLocalOauthPersistenceProvider persist) {
		super(ctx);
		mPersist = persist;

	}

	@Override
	public void setDef(
			RestListDef serviceDef,
			RestAsyncRespnseHandler<TwitterRestSearchResponseItem, TwitterSearchResponse> handler,
			boolean useCache) {
			mHandler = handler;
			mServiceDef = serviceDef;
		// TODO useCache

		// handle oauth
			
		

	}

	@Override
	public void search(String userQuery, int start, int num) {
			
		SearchTwitter search = new SearchTwitter();
		search.execute(userQuery);
	}
	
	
	 class SearchTwitter extends AsyncTask<String, String, String> {
		 
	        /**
	         * Before starting background thread Show Progress Dialog
	         * */
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	           
	        }
	 

	        protected String doInBackground(String... args) {

	            try {
	                ConfigurationBuilder builder = new ConfigurationBuilder();
	                builder.setOAuthConsumerKey(mServiceDef.getOAuthConsumerKey());
	                builder.setOAuthConsumerSecret(mServiceDef.getOauthConsumerSecret());
	                 
	                // Access Token
	                String access_token = mPersist.getOauthToken(mServiceDef);
	                // Access Token Secret
	                String access_token_secret = mPersist.getOauthSecret(mServiceDef);
	                 
	                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
	                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
	                 
	                Query query = new Query(args[0]);
	        		query.setCount(100);
	        		query.setSinceId(0);
	        		
	        		QueryResult result = null;
	        		
	        		result = twitter.search(query);
	        		
	        			if(result !=null)
	        				for (twitter4j.Status status  :  result.getTweets()) {
	        					System.out.println(status.getText());
	        				}
	        		} catch (TwitterException e) {
	        			
	        			Log.d(tag, e.getMessage() + " >>> " + e.getStatusCode());
	        			mHandler.onError(ErrorCodes.SERVICE_UNAUTHORIZED);
	        			
	        		} catch (Exception e) {

	        			Log.d(tag, e.getMessage());
	        			mHandler.onError(ErrorCodes.SERVICE_UNAUTHORIZED);
	        		}
	                
	                
	            return null;
	        }
	 

	        protected void onPostExecute(String results) {
	           
	        }
	 
	    }

}
