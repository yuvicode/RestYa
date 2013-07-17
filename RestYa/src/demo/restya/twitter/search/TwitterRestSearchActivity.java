package demo.restya.twitter.search;

import java.util.List;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import android.content.pm.ActivityInfo;
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
import code.java.restya.app.ui.OauthAuthorizeHandler;
import code.java.restya.app.ui.OauthCallBackUrlHandler;
import code.java.restya.app.ui.OauthSecretHandler;
import code.java.restya.app.ui.OauthWebView;
import code.java.restya.core.ConnectionDetectorProvider;
import code.java.restya.core.ErrorCodes;
import code.java.restya.core.RestAsyncRespnseHandler;
import code.java.restya.core.RestAuthenticationProvider;
import code.java.restya.core.RestLocalOauthPersistenceProvider;
import code.java.restya.core.RestOauthSecretVerifier;
import code.java.restya.core.RestSearchProvider;
import code.java.restya.providers.twitter.TwitterRestSearchResponseItem;
import code.java.restya.providers.twitter.TwitterSearchResponse;

import com.google.inject.Inject;
import com.google.inject.util.Modules;

public class TwitterRestSearchActivity extends RoboActivity implements
		ScrollResultsDown {

	// the service definitions
	private final RestListDef restyaTwitterDemoServiceDef = RestListDef.TWITTER_SEARCH;

	// Services
	@Inject
	private ConnectionDetectorProvider cd;

	@Inject
	private AppErrorProvider appErrorProvider;

	@Inject
	private RestSearchProvider<TwitterRestSearchResponseItem, TwitterSearchResponse> searchProvider;

	@Inject
	private RestLocalOauthPersistenceProvider restLocalOauthPersistenceProvider;

	@Inject
	private RestAuthenticationProvider restAuthenticationProvider;

	private OauthAuthorizeHandler loginHandler;

	private RestAsyncRespnseHandler<TwitterRestSearchResponseItem, TwitterSearchResponse> handler;

	// UI elements
	Button btnLoginTwitter;

	View mainView;

	View oauthWin;

	FrameLayout container;

	LinearLayout twitterSearchcontainer, resultsContainer;

	EditText searchTxt;

	ProgressBar loadingProgressBar;
	// webview for oauth "dance" see -
	// http://www.cubrid.org/blog/dev-platform/dancing-with-oauth-understanding-how-authorization-works/
	@Inject
	OauthWebView oauthWebView;

	// Application logic elements

	String lastSearch = "";

	// this is used to prevent many events from the ScrollView reach bottom
	boolean isLoadingMore = true;
	// keep track on results index
	int lastResultIndex = 0;
	// results "page: size
	final int RESULTS_PAGE_SIZE = 15;

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// define project Guice module
		RoboGuice.setBaseApplicationInjector(getApplication(),
				RoboGuice.DEFAULT_STAGE, Modules.combine(
						RoboGuice.newDefaultRoboModule(getApplication()),
						new TwitterSearchModule()));

		super.onCreate(savedInstanceState);

		// define your service attributes
		restyaTwitterDemoServiceDef.setOAuthConsumerKey("6YJCS09S2ULtdvYWsXDg");
		restyaTwitterDemoServiceDef
				.setOauthConsumerSecret("oos1HBsFLcjh0lPqG3y8wuzucH3e91I8l3kUuZ8FBdA");
		// url for the end of the process
		restyaTwitterDemoServiceDef
				.setCallBackUrl("https://sites.google.com/site/twitterdemopage/");

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
		// Set handler for scroll to bottom
		scrollResults.setScrollResultsDown(this);

		// set the default view
		toggleViews(mainView);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Internet connection check
		if (!cd.isConnectingToInternet()) {
			appErrorProvider.showError(this, ErrorCodes.ERR_NO_CONNECTION);
		}
		else{
			if(restAuthenticationProvider.isLoggedIn(restyaTwitterDemoServiceDef)){
				twitterSearchcontainer.setVisibility(View.VISIBLE);
			}
			else
			{
				btnLoginTwitter.setVisibility(View.VISIBLE);
			}
		}

		
				
		/*
		 * Handler for verify the token - last oauth step
		 * This will enable UI interaction as a result of the last step of oauth process
		 * user token and secret are stored using the RestLocalOauthPersistenceProvider in the 
		 * implementation of RestOauthSecretVerifier
		 */
		final OauthSecretHandler verifyHandler = new OauthSecretHandler() {

			@Override
			public void onSuccess() {
				btnLoginTwitter.setVisibility(View.GONE);
				twitterSearchcontainer.setVisibility(View.VISIBLE);
				toggleViews(mainView);

			}

			@Override
			public void onFail(ErrorCodes error) {
				if (error != ErrorCodes.USER_ABORT)
					appErrorProvider.showError(TwitterRestSearchActivity.this,
							error);
				btnLoginTwitter.setVisibility(View.VISIBLE);
				twitterSearchcontainer.setVisibility(View.GONE);
				toggleViews(mainView);

			}

		};

		/* handler for login (first step authorization)
		 * the (success) results are sent to the UI for oauth "dance" in the webview (or other UI implementation)
		 * The returned RestOauthSecretVerifier implementation will be used after the the webview step 
		 * to verify and sore the the user token an secret 
		 * 
		 */
		loginHandler = new OauthAuthorizeHandler() {

			@Override
			public void onSuccess(final String url,
					final RestOauthSecretVerifier verify) {

				oauthWebView.start(url,
						restyaTwitterDemoServiceDef.getCallBackUrl(),
					/* handler for the web view  - second step handler
					 * Results (success) are sent to the  RestOauthSecretVerifier to complete the process 
					 */
					new OauthCallBackUrlHandler() {

							@Override
							public void onSuccess(String tokenUrl) {
								btnLoginTwitter.setVisibility(View.GONE);
								toggleViews(mainView);
								verify.verify( tokenUrl, verifyHandler);

							}

							@Override
							public void onFail(ErrorCodes error) {

								if (error != ErrorCodes.USER_ABORT)
									appErrorProvider.showError(
											TwitterRestSearchActivity.this,
											error);

								toggleViews(mainView);

							}	

						});
			}

			@Override
			public void onFail(ErrorCodes error) {

				toggleViews(mainView);
				
				if (error != ErrorCodes.USER_ABORT)
					appErrorProvider.showError(TwitterRestSearchActivity.this,
							error);
				
				
			}
		};

		
		
		
		
		// search results handler to perform for REST call results
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

				lastResultIndex = lastResultIndex + items.size();

				for (TwitterRestSearchResponseItem item : items) {

					FrameLayout frame = (FrameLayout) getLayoutInflater()
							.inflate(R.layout.result_item, null);

					TextView resultTxt = (TextView) frame
							.findViewById(R.id.resultTxt);
					resultTxt.setText(Html.fromHtml(item.getText()));

					resultsContainer.addView(frame);

				}
				// can reload more ...
				isLoadingMore = !response.hasMore();
			}
		};
		// Set params to search service
		searchProvider.setDef(restyaTwitterDemoServiceDef, handler, false);

		/**
		 * Login button click
		 * 
		 * */
		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!cd.isConnectingToInternet()) {
					appErrorProvider.showError(TwitterRestSearchActivity.this,
							ErrorCodes.ERR_NO_CONNECTION);
				} else {
					toggleViews(oauthWin);

					restAuthenticationProvider.login(
							restyaTwitterDemoServiceDef, loginHandler);

				}

			}
		});

		/*
		 * Handling the user input in the search box
		 * 
		 * If string has changed ... new search
		 * 
		 * If no change (space, or delete a space) then ignore...
		 */
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
						searchProvider.search(str, lastResultIndex,
								RESULTS_PAGE_SIZE);
					}

				}

			}
		};
		searchTxt.addTextChangedListener(inputTextWatcher);
		
		
		

	}

	// toggle the main view and oauth win during "registration" process
	private void toggleViews(View view) {
		container.removeAllViews();
		container.addView(view);
	}

	// Scroll results reach bottom
	/*
	 * (non-Javadoc)
	 * 
	 * @see demo.restya.ScrollResultsDown#onBottomReach()
	 * 
	 * This will happen every time therefore need to control
	 */
	@Override
	public void onBottomReach() {

		if (!isLoadingMore) {
			loadingProgressBar.setVisibility(View.VISIBLE);
			searchProvider.search(lastSearch, lastResultIndex,
					RESULTS_PAGE_SIZE);
		}

	}

	protected void onResume() {
		super.onResume();
	}

}
