package code.java.restya.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import code.java.restya.R;
import code.java.restya.RestListDef;
import code.java.restya.core.ErrorCodes;

import com.google.inject.Inject;

/**
 * This resource is designed to provide the UI for the oauth process including:
 * <li>Local (asset file) to handle service loading latency <li>The service
 * provider authorization page <li>The application (your) END page url as YOU
 * defined in the service provider web-site, and in the application
 * {@link RestListDef#setCallBackUrl()}
 * <p>
 * It is possible to add more localized as: <br>
 * /assets/web/restya/restya_oauth_init-{local}.html <br>
 * {local}: lower case code: fr,gr,ru .....
 * </p>
 */
public class OauthWebView {

	private WebView webWin;

	private Button cancelBtn;
	
	private Button completeBtn;
	
	private boolean hasError = false;

	// TODO: add internatiolization for

	Context ctx;

	@Inject
	public OauthWebView(Context ctx) {
		this.ctx = ctx;
	}

	public void init(View view) {

		webWin = (WebView) view.findViewById(R.id.oauthWebView);

		cancelBtn = (Button) view.findViewById(R.id.btnCancel);
		completeBtn = (Button) view.findViewById(R.id.btnComplete);

		webWin.loadUrl(getOauthStartPage());

	}

	@SuppressLint("SetJavaScriptEnabled")
	public void start(final String startUrl, final String endUrl,
			final OauthCallBackUrlHandler handler) {

		hasError = false;
		
		webWin.setVisibility(View.VISIBLE);

		webWin.loadUrl(startUrl);

		webWin.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {

				// reached the last oauth page with no errors
				if (url.startsWith(endUrl) && !hasError) {
					completeBtn.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				hasError = true;

				failed(ErrorCodes.ERR_NO_CONNECTION, handler);

				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		webWin.getSettings().setJavaScriptEnabled(true);

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				failed(ErrorCodes.USER_ABORT, handler);

			}
		});

		completeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				completeBtn.setVisibility(View.GONE);
				handler.onSuccess(webWin.getUrl());

			}
		});

	}

	private void failed(ErrorCodes error, OauthCallBackUrlHandler handler) {
		completeBtn.setVisibility(View.GONE);
		webWin.loadUrl(getOauthStartPage());
		handler.onFail(error);
	}

	/**
	 * lockup for localized oauth process start page....
	 */
	private String getOauthStartPage() {

		final String assetsRoot = "file:///android_asset/";
		final String restyaAssetsFolder = "web/restya/";
		final String defaultoauthProcessStartPage = "restya_oauth_init";

		// set default
		String page = assetsRoot + restyaAssetsFolder
				+ defaultoauthProcessStartPage + ".html";

		String locale;
		try {
			locale = ctx.getResources().getConfiguration().locale.getCountry();

			String localedFile = restyaAssetsFolder
					+ defaultoauthProcessStartPage + "-" + locale + ".html";

			localedFile = localedFile.toLowerCase();

			ctx.getAssets().open(localedFile);

			page = assetsRoot + localedFile;

		} catch (Exception e) {
			// do nothing..
		}

		return page;
	}
}
