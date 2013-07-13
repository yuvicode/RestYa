package code.java.restya.providers.twitter;


import android.content.Context;
import code.java.restya.RestListDef;
import code.java.restya.core.RestAsyncRespnseHandler;
import code.java.restya.core.RestLocalOauthPersistenceProvider;
import code.java.restya.core.RestSearchProvider;

import com.google.inject.Inject;


public class TwitterSimpleSearchRestProvider extends TwitterRestProvider
		implements
		RestSearchProvider<TwitterRestSearchResponseItem, TwitterSearchResponse> {

	
	private final String tag = TwitterSimpleSearchRestProvider.class.getSimpleName();
	
	
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


	}

	

	
	@Override
	public void search(String userQuery, int start, int num) {
			
		SearchTwitter search = new SearchTwitter(mServiceDef,mHandler, mPersist,start,num);
		search.execute(userQuery);
	}
	
	
	 
}
