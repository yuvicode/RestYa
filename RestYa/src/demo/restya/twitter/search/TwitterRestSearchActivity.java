package demo.restya.twitter.search;

import java.util.List;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import code.java.restya.R;
import code.java.restya.RestListDef;
import code.java.restya.app.ui.AppErrorProvider;
import code.java.restya.app.ui.OauthSuccessHandler;
import code.java.restya.app.ui.OauthWebView;
import code.java.restya.core.ConnectionDetectorProvider;
import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestAsyncRespnseHandler;
import code.java.restya.core.RestLocalOauthPersistenceProvider;
import code.java.restya.core.RestSearchProvider;
import code.java.restya.providers.twitter.TwitterRestSearchResponseItem;
import code.java.restya.providers.twitter.TwitterSearchResponse;

import com.google.inject.Inject;
import com.google.inject.util.Modules;

public class TwitterRestSearchActivity extends RoboActivity implements
		ScrollResultsDown {

	/**
	 * deprecated ---- need to move to RestAuthenticationProvider
	 * */
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	/**
	 * deprecated ---- need to move to RestAuthenticationProvider
	 * */
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	/**
	 * deprecated ---- need to move to RestAuthenticationProvider
	 * */
	private static Twitter twitter;
	private static RequestToken requestToken;

	// Services
	@Inject
	private ConnectionDetectorProvider cd;

	@Inject
	private AppErrorProvider appErrorProvider;

	@Inject
	private RestSearchProvider<TwitterRestSearchResponseItem, TwitterSearchResponse> searchProvider;

	@Inject
	private RestLocalOauthPersistenceProvider restLocalOauthPersistenceProvider;

	private RestAsyncRespnseHandler<TwitterRestSearchResponseItem, TwitterSearchResponse> handler;

	// UI elements
	Button btnLoginTwitter;

	View mainView;

	View oauthWin;

	FrameLayout container;

	LinearLayout twitterSearchcontainer, resultsContainer;

	EditText searchTxt;

	ProgressBar loadingProgressBar;

	String lastSearch = "";

	// this is used to prevent many events from the
	boolean isLoadingMore = true;

	int lastResultIndex = 0;
	
	final int RESULTS_PAGE_SIZE = 15; 

	@Inject
	OauthWebView oauthWebView;

	// toggle the main view and oauth win during registration process
	private void toggleViews(View view) {
		container.removeAllViews();
		container.addView(view);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// define project Guice module
		RoboGuice.setBaseApplicationInjector(getApplication(),
				RoboGuice.DEFAULT_STAGE, Modules.combine(
						RoboGuice.newDefaultRoboModule(getApplication()),
						new TwitterSearchModule()));

		super.onCreate(savedInstanceState);

		mainView = getLayoutInflater().inflate(R.layout.main, null);

		oauthWin = getLayoutInflater().inflate(R.layout.oauth_web_view, null);

		oauthWebView.init(oauthWin);

		setContentView(R.layout.container);
		container = (FrameLayout) findViewById(R.id.main_area);

		btnLoginTwitter = (Button) mainView.findViewById(R.id.btnLoginTwitter);
		twitterSearchcontainer = (LinearLayout) mainView
				.findViewById(R.id.twitterSearchcontainer);

		resultsContainer = (LinearLayout) mainView
				.findViewById(R.id.resultsContainer);

		searchTxt = (EditText) mainView.findViewById(R.id.searchTxt);

		loadingProgressBar = (ProgressBar) mainView
				.findViewById(R.id.loadingProgressBar);

		TwitterRestScrollView scrollResults = (TwitterRestScrollView) mainView
				.findViewById(R.id.scrollResults);
		// set handler
		scrollResults.setScrollResultsDown(this);

		toggleViews(mainView);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if (!cd.isConnectingToInternet()) {
			appErrorProvider.showError(this, ErrorCodes.ERR_NO_CONNECTION);
		}

		// search results handler
		handler = new RestAsyncRespnseHandler<TwitterRestSearchResponseItem, TwitterSearchResponse>() {

			@Override
			public void onError(ErrorCodes erorCodes) {
				// on error
				appErrorProvider.showError(TwitterRestSearchActivity.this,
						erorCodes);
				loadingProgressBar.setVisibility(View.VISIBLE);

			}

			@Override
			public void onSuccess(TwitterSearchResponse response) {

				loadingProgressBar.setVisibility(View.VISIBLE);

				List<TwitterRestSearchResponseItem> items = response.getItems();

				lastResultIndex = lastResultIndex  + items.size();
				
				for (TwitterRestSearchResponseItem item : items) {

					FrameLayout frame = (FrameLayout) getLayoutInflater()
							.inflate(R.layout.result_item, null);

					TextView resultTxt = (TextView) frame
							.findViewById(R.id.resultTxt);
					resultTxt.setText(Html.fromHtml(item.getText()));

					resultsContainer.addView(frame);

					
				}
				isLoadingMore = false;
			}
		};
		// set params to search service
		searchProvider.setDef(RestListDef.TWITTER_SEARCH, handler, false);

		/**
		 * Twitter login button click event will call loginToTwitter() function
		 * */
		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!cd.isConnectingToInternet()) {
					appErrorProvider.showError(TwitterRestSearchActivity.this,
							ErrorCodes.ERR_NO_CONNECTION);
				} else
					loginToTwitter();
			}
		});

		TextWatcher inputTextWatcher = new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence chars, int start,
					int before, int count) {
				if (chars != null) {
					String str = chars.toString();
					if (str != null && str.length() > 0
							&& !lastSearch.trim().equals(str.trim())) {
						lastSearch = str;
						resultsContainer.removeAllViews();
						loadingProgressBar.setVisibility(View.VISIBLE);
						lastResultIndex = 0;
						searchProvider.search(str,lastResultIndex, RESULTS_PAGE_SIZE);
					}

				}

			}
		};
		searchTxt.addTextChangedListener(inputTextWatcher);

	}

	/**
	 * deprecated ---- need to move to RestAuthenticationProvider
	 * */
	private void setTokenAndSecret(String url) {
		// Uri uri = getIntent().getData();

		final Uri uri = Uri.parse(url);
		if (uri != null
				&& uri.toString().startsWith(
						RestListDef.TWITTER_SEARCH.getCallBackUrl())) {
			// oAuth verifier

			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						String verifier = uri
								.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
						// Get the access token

						btnLoginTwitter.setVisibility(View.GONE);

						final AccessToken accessToken = twitter
								.getOAuthAccessToken(requestToken, verifier);

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								restLocalOauthPersistenceProvider
										.setOauthSecret(
												RestListDef.TWITTER_SEARCH,
												accessToken.getTokenSecret());
								restLocalOauthPersistenceProvider
										.setOauthToken(
												RestListDef.TWITTER_SEARCH,
												accessToken.getToken());

								twitterSearchcontainer
										.setVisibility(View.VISIBLE);

							}
						});

					} catch (Exception e) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								btnLoginTwitter.setVisibility(View.VISIBLE);
								twitterSearchcontainer.setVisibility(View.GONE);

							}
						});
					}

				}
			}).start();

		}
	}

	/**
	 * deprecated ---- need to move to RestAuthenticationProvider
	 * */

	private void loginToTwitter() {
		// Check if already logged in

		toggleViews(oauthWin);

		new Thread(new Runnable() {

			@Override
			public void run() {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(RestListDef.TWITTER_SEARCH
						.getOAuthConsumerKey());
				builder.setOAuthConsumerSecret(RestListDef.TWITTER_SEARCH
						.getOauthConsumerSecret());
				Configuration configuration = builder.build();

				TwitterFactory factory = new TwitterFactory(configuration);
				twitter = factory.getInstance();

				try {
					requestToken = twitter
							.getOAuthRequestToken(RestListDef.TWITTER_SEARCH
									.getCallBackUrl());

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							oauthWebView.start(
									requestToken.getAuthenticationURL(),
									RestListDef.TWITTER_SEARCH.getCallBackUrl(),
									new OauthSuccessHandler() {

										@Override
										public void onSuccess(String param) {
											toggleViews(mainView);
											setTokenAndSecret(param);
										}

										@Override
										public void onFail(String param) {

											toggleViews(mainView);

										}

									});

						}
					});

				} catch (TwitterException e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							toggleViews(mainView);

						}
					});

					e.printStackTrace();
				}

			}
		}).start();

	}

	protected void onResume() {
		super.onResume();
	}

	// scroll reach bottom
	/*
	 * (non-Javadoc)
	 * 
	 * @see demo.restya.ScrollResultsDown#onBottomReach()
	 * 
	 * This will happen every time therefore need to control
	 */
	@Override
	public void onBottomReach() {

		if (!isLoadingMore)
		{
			loadingProgressBar.setVisibility(View.VISIBLE);
			searchProvider.search(lastSearch,lastResultIndex , RESULTS_PAGE_SIZE);
		}
		
	}

}
