package code.java.restya.providers.twitter;


import java.util.ArrayList;
import java.util.List;

import code.java.restya.core.RestServiceResponse;

/**
 * provide a wrapper for twitter search response
 *
 */
public class TwitterSearchResponse extends TwitterBasicRestResponse implements RestServiceResponse<TwitterRestSearchResponseItem>{

	
	List<TwitterRestSearchResponseItem> items = new ArrayList<TwitterRestSearchResponseItem>();
	


	public TwitterSearchResponse(boolean isSuccess, String errorMsg,
			int length, boolean hasMore) {
		super(isSuccess, errorMsg, length, hasMore);
	}

	@Override
	public List<TwitterRestSearchResponseItem> getItems() {
		// TODO Auto-generated method stub
		return items;
	}
	
	public void addItem(TwitterRestSearchResponseItem item){
		items.add(item);
	}

	
	

}
