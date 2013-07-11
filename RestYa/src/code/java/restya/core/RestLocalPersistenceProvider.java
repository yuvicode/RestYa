package code.java.restya.core;

import code.java.restya.RestListDef;

/**
 * provider for local params storage
 *
 */
public interface RestLocalPersistenceProvider {
	public void setServiceString(RestListDef restDef,String name,String str);
	public void setServiceInt(RestListDef restDef, String name,int num);
	
	public String getServiceString(RestListDef restDef,String name);
	public int getServiceInt(RestListDef restDef, String name);
	
	
	public void setProviderString(RestListDef restDef,String name,String str);
	public void setProviderInt(RestListDef restDef, String name, int num);

	
	public String getProviderString(RestListDef restDef,String name);
	public int getProviderInt(RestListDef restDef, String name);
	
	
	public void clearProvider(RestListDef restDef, String name);
	
	public void clearProvider(RestListDef restDef);
	
	public void clearService(RestListDef restDef, String name);
	
	public void clearService(RestListDef restDef);
}
