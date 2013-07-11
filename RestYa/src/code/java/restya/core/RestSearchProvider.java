package code.java.restya.core;

import code.java.restya.RestListDef;

/**
 * Logical layer to perform before and after the the actual rest search call
 *
 */
public interface RestSearchProvider <I extends RestResponsetServiceItem, T extends RestServiceResponse<I> >{


	public void setDef(RestListDef serviceDef, RestAsyncRespnseHandler<I,T>  handler, boolean useCache);
	
	public void search(String query, int start, int num);
}
