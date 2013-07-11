package code.java.restya.core;

import java.util.List;


/**
 * 
* Represent a rest response object
 */
public interface RestServiceResponse <T extends RestResponsetServiceItem>{

	boolean isSuccess();
	String getError();
	int length();
	boolean hasMore();
	List<T> getItems();
	
	
}
