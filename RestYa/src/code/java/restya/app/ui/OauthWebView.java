package code.java.restya.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import code.java.restya.R;

import com.google.inject.Inject;

public class OauthWebView {

	private WebView webWin;

	private Button cancelBtn;
	private Button completeBtn;
	private final String oauthProcessStartPage = "file:///android_asset/web/reatya_oauth_init.html";
	

	Context ctx;

	@Inject
	public OauthWebView(Context ctx) {
		this.ctx = ctx;
	}

	public void init(View view) {

		webWin = (WebView) view.findViewById(R.id.oauthWebView);

		cancelBtn = (Button) view.findViewById(R.id.btnCancel);
		completeBtn = (Button) view.findViewById(R.id.btnComplete);
		
		
		webWin.loadUrl(oauthProcessStartPage);
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void start(final String startUrl, final String endUrl,
			final OauthSuccessHandler handler) {

		webWin.setVisibility(View.VISIBLE);
		
		webWin.loadUrl(startUrl);

		webWin.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
	
				if(url.startsWith(endUrl)){
					completeBtn.setVisibility(View.VISIBLE);
				}
			}
			
			 @Override
			   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			       if(errorCode==WebViewClient.ERROR_FILE_NOT_FOUND)
			            	webWin.setVisibility(View.GONE);
			            
			        super.onReceivedError(view, errorCode, description, failingUrl);
			  }
		});
		webWin.getSettings().setJavaScriptEnabled(true);

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				completeBtn.setVisibility(View.GONE);
				webWin.loadUrl(oauthProcessStartPage);
				handler.onFail(webWin.getUrl());
			}
		});
		
		completeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.onSuccess(webWin.getUrl());
			}
		});

	}
}
