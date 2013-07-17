package code.java.restya.providers.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.util.Log;
import code.java.restya.RestListDef;
import code.java.restya.app.ui.OauthAuthorizeHandler;
import code.java.restya.app.ui.OauthSecretHandler;
import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestLocalOauthPersistenceProvider;
import code.java.restya.core.RestOauthSecretVerifier;

/**
 * This class perform the Async Token request
 *
 */
class TwitterAsyncOAuthTokenRequest extends AsyncTask<String, String, String> {

	protected static final String OauthSecretHandler = null;

	boolean hasError = false;

	RestListDef restyaTwitterDemoServiceDef;

	RestLocalOauthPersistenceProvider mPersist;

	ErrorCodes error = ErrorCodes.ERR_UNKNOWN;

	Twitter twitter;

	RequestToken requestToken = null;
	
	OauthAuthorizeHandler mHandler;

	TwitterAsyncOAuthTokenRequest(RestListDef restyaTwitterDemoServiceDef,
		RestLocalOauthPersistenceProvider persist,OauthAuthorizeHandler handler) {
		this.restyaTwitterDemoServiceDef = restyaTwitterDemoServiceDef;
		mPersist = persist;
		mHandler = handler;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	protected String doInBackground(String... args) {

		ConfigurationBuilder builder = new ConfigurationBuilder();

		builder.setOAuthConsumerKey(restyaTwitterDemoServiceDef
				.getOAuthConsumerKey());

		builder.setOAuthConsumerSecret(restyaTwitterDemoServiceDef
				.getOauthConsumerSecret());
		Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		try {
			requestToken = twitter
					.getOAuthRequestToken(restyaTwitterDemoServiceDef
							.getCallBackUrl());

			hasError = false;
		} catch (TwitterException e) {
			hasError = true;
			try {
				if (e.getCause().getClass().getName().equals("java.net.SocketTimeoutException"))
					error = ErrorCodes.ERR_PAGE_CANT_DISPLAY;
				else
					error = ErrorCodes.SERVICE_UNAUTHORIZED;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(e!=null && e.getErrorMessage()!=null && e.getErrorMessage().length() > 0)
				Log.d(TwitterAsyncOAuthTokenRequest.class.getSimpleName(), e.getErrorMessage());
			
		} catch (Exception e) {
			if(e!=null && e.getMessage() !=null && e.getMessage().length() > 0)
			Log.d(TwitterAsyncOAuthTokenRequest.class.getSimpleName(), e.getMessage());

			hasError = true;
			error = ErrorCodes.ERR_UNKNOWN;
		}

		return null;

	}

	protected void onPostExecute(String results) {

		if (hasError == true) {
			mHandler.onFail(error);

		} else {
			if(requestToken!=null && requestToken.getAuthenticationURL()!=null ){
				
				RestOauthSecretVerifier secretHandler = new RestOauthSecretVerifier(){

					@Override
					public void verify(String token, OauthSecretHandler handler) {
						new TwitterAsyncOAuthSecretRequest(restyaTwitterDemoServiceDef,  mPersist, requestToken, handler).execute(token);
						
					}};

				
				mHandler.onSuccess(requestToken.getAuthenticationURL(),  secretHandler);
			}
				
			else
				mHandler.onFail(error);
		}
	}

}
