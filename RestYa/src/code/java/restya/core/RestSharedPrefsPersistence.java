package code.java.restya.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import code.java.restya.RestListDef;
import code.java.restya.RestProviders;

import com.google.inject.Inject;

public class RestSharedPrefsPersistence implements RestLocalPersistenceProvider{

	Context mCtx;
	
	@Inject
	RestSharedPrefsPersistence(Context ctx){
		mCtx = ctx;
	}
	
	// shared pref for the service
	private SharedPreferences mServiceSharedPreferences = null;
	
	// shared pref for the provider (common to all services of the same provider)
	private SharedPreferences mProviderSharedPreferences = null;
		

	private  SharedPreferences getPreff(RestListDef service) {
		if(mServiceSharedPreferences == null)
		mServiceSharedPreferences = mCtx.getSharedPreferences(service.name(),Context.MODE_PRIVATE);
		
		return mServiceSharedPreferences;
	}
	
	private SharedPreferences getPreff(RestProviders provider) {
		if(mProviderSharedPreferences == null)
		mProviderSharedPreferences = mCtx.getSharedPreferences(provider.name(),
				Context.MODE_PRIVATE);
		
		return mProviderSharedPreferences;
	}
	
	
	@Override
	public void setServiceString(RestListDef restDef,String name, String str) {
		SharedPreferences  pref = getPreff(restDef);
		
		Editor e = pref.edit();
		e.putString(name,str);
		
		e.commit(); 
		
	}

	@Override
	public void setServiceInt(RestListDef restDef, String name,int num) {
		SharedPreferences  pref = getPreff(restDef);
		Editor e = pref.edit();
		e.putInt(name,num);
		e.commit(); 
		
	}

	@Override
	public void setProviderString(RestListDef restDef,String name, String str) {
		SharedPreferences  pref = getPreff(restDef.getRestProvider());
		Editor e = pref.edit();
		e.putString(name,str);
		e.commit(); 
		
	}

	@Override
	public void setProviderInt(RestListDef restDef,String name, int num) {
		SharedPreferences  pref = getPreff(restDef.getRestProvider());
		Editor e = pref.edit();
		e.putInt(name,num);
		e.commit();
	}

	@Override
	public String getServiceString(RestListDef restDef, String name) {
		SharedPreferences  pref = getPreff(restDef);
		
		return pref.getString(name, null);
	}

	@Override
	public int getServiceInt(RestListDef restDef, String name) {
		SharedPreferences  pref = getPreff(restDef);
		return pref.getInt(name,0);
	}

	@Override
	public String getProviderString(RestListDef restDef, String name) {
		SharedPreferences  pref = getPreff(restDef.getRestProvider());
		return pref.getString(name, null);
	}

	@Override
	public int getProviderInt(RestListDef restDef, String name) {
		SharedPreferences  pref = getPreff(restDef.getRestProvider());
		return pref.getInt(name,0);
	}

	@Override
	public void clearProvider(RestListDef restDef, String name) {
		SharedPreferences  pref = getPreff(restDef.getRestProvider());
		Editor edit = pref.edit();
		edit.remove(name);
		edit.commit();
	}

	@Override
	public void clearProvider(RestListDef restDef) {
		SharedPreferences  pref = getPreff(restDef.getRestProvider());
		Editor edit = pref.edit();
		edit.clear();
		edit.commit();
		
	}

	@Override
	public void clearService(RestListDef restDef, String name) {
		SharedPreferences  pref = getPreff(restDef);
		Editor edit = pref.edit();
		edit.remove(name);
		edit.commit();
	}

	@Override
	public void clearService(RestListDef restDef) {
		SharedPreferences  pref = getPreff(restDef);
		Editor edit = pref.edit();
		edit.clear();
		edit.commit();
		
	}


	

}
