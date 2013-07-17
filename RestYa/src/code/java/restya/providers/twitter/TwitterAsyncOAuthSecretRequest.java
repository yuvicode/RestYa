package code.java.restya.providers.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.net.Uri;
import android.os.AsyncTask;
import code.java.restya.RestListDef;
import code.java.restya.app.ui.OauthSecretHandler;
import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestLocalOauthPersistenceProvider;

/**
 * This class perform the Async secret request
 * 
 */
class TwitterAsyncOAuthSecretRequest extends AsyncTask<String, String, String> {


	boolean hasError = false;

	RestListDef restyaTwitterDemoServiceDef;

	RestLocalOauthPersistenceProvider mPersist;

	ErrorCodes error = ErrorCodes.ERR_UNKNOWN;

	Twitter twitter;

	RequestToken requestToken;
	
	AccessToken accessToken;
	
	OauthSecretHandler mHandler;

	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

	TwitterAsyncOAuthSecretRequest(RestListDef restyaTwitterDemoServiceDef,
			RestLocalOauthPersistenceProvider persist, RequestToken requestToken, OauthSecretHandler handler) {
		this.restyaTwitterDemoServiceDef = restyaTwitterDemoServiceDef;
		mPersist = persist;
		this.requestToken = requestToken;
		mHandler = handler;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	protected String doInBackground(String... token) {

		final Uri uri = Uri.parse(token[0]);
		if (uri != null
				&& uri.toString().startsWith(
						restyaTwitterDemoServiceDef.getCallBackUrl())) {

			try {
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				ConfigurationBuilder builder = new ConfigurationBuilder();

				builder.setOAuthConsumerKey(restyaTwitterDemoServiceDef
						.getOAuthConsumerKey());

				builder.setOAuthConsumerSecret(restyaTwitterDemoServiceDef
						.getOauthConsumerSecret());
				Configuration configuration = builder.build();

				TwitterFactory factory = new TwitterFactory(configuration);
				twitter = factory.getInstance();

				accessToken = twitter
						.getOAuthAccessToken(requestToken, verifier);
				
				hasError = false;
			} catch (TwitterException e) {
				hasError = true;
				 error = ErrorCodes.SERVICE_UNAUTHORIZED;
				e.printStackTrace();
			}
			 catch (Exception e) {
				 hasError = true;
				 e.printStackTrace();
			}
		}

		return null;

	}

	protected void onPostExecute(String results) {

		if (hasError == false && accessToken!=null) {
			mPersist
			.setOauthSecret(
					restyaTwitterDemoServiceDef,
					accessToken.getTokenSecret());
			mPersist
			.setOauthToken(
					restyaTwitterDemoServiceDef,
					accessToken.getToken());
			mHandler.onSuccess();
		}
		else
			mHandler.onFail(error);
	}

}
