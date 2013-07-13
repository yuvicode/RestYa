package code.java.restya.providers.twitter;

import code.java.restya.core.BasicRestResponse;

/**
 * class of common Twitter rest response functionality .... 
 *
 */
public abstract class TwitterBasicRestResponse extends BasicRestResponse {

	public TwitterBasicRestResponse(boolean isSuccess, String errorMsg,
			int length, boolean hasMore) {
		super(isSuccess, errorMsg, length, hasMore);
	}

}
