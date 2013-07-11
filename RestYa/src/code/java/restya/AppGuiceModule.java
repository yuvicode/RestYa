package code.java.restya;

import code.java.restya.app.ui.AppErrorProvider;
import code.java.restya.app.ui.SimpleToastErrorPresenter;
import code.java.restya.core.ConnectionDetector;
import code.java.restya.core.ConnectionDetectorProvider;
import code.java.restya.core.RestAuthenticationProvider;
import code.java.restya.core.RestLocalOauthPersistenceProvider;
import code.java.restya.core.RestLocalOauthSharedPrefPersistence;
import code.java.restya.core.RestLocalPersistenceProvider;
import code.java.restya.core.RestOauthAuthentication;
import code.java.restya.core.RestSearchProvider;
import code.java.restya.core.RestSharedPrefsPersistence;
import code.java.restya.providers.twitter.TwitterRestSearchResponseItem;
import code.java.restya.providers.twitter.TwitterSearchResponse;
import code.java.restya.providers.twitter.TwitterSimpleSearchRestProvider;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * Application D definitions
 *
 */
public class AppGuiceModule  extends AbstractModule {

	@Override
	protected void configure() {
		
	bind(ConnectionDetectorProvider.class).to(ConnectionDetector.class);

	// ui error implementation
	bind(AppErrorProvider.class).to(SimpleToastErrorPresenter.class);
	
	// general shared pref persistence
	bind(RestLocalPersistenceProvider.class).to(RestSharedPrefsPersistence.class);
	
	// oauth params persistence
	bind(RestLocalOauthPersistenceProvider.class).to(RestLocalOauthSharedPrefPersistence.class);
	
	// oauth authentication
	bind(RestAuthenticationProvider.class).to(RestOauthAuthentication.class);
	

	// the REST search service to the twitter implementation 

	bind(new TypeLiteral<RestSearchProvider<TwitterRestSearchResponseItem,TwitterSearchResponse>>(){}).to(TwitterSimpleSearchRestProvider.class);
	
	}

}
