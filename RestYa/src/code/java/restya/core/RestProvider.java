package code.java.restya.core;

import android.content.Context;
import code.java.restya.RestListDef;

import com.google.inject.Inject;

/**
 * General purpose rest services for a rest call
 *
 */
public abstract class RestProvider {

	protected RestListDef mServiceDef;
	protected boolean mUseCache; 
	

	
	protected void addToCache(String url){
		// TODO add cacheing mechanism
	}
	
	protected void getResultFromCache(String url){
		// TODO add cahcheing mechanism
	}
	
	
	private Context mCtx;
	

	@Inject
	protected RestProvider(Context ctx) {
		 mCtx = ctx; 
		
	}
	

	
	
	
}
