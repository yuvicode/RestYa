package code.java.restya.core;



/**
 * Generic handler for rest calls
 */
public   interface  RestAsyncRespnseHandler <I extends RestResponsetServiceItem,T extends RestServiceResponse<I>>{

	void onError(ErrorCodes erorCodes);
	void onSuccess(T response);
}
