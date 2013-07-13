package code.java.restya.providers.twitter;

import java.util.Date;

import code.java.restya.core.RestResponsetServiceItem;

/**
 * wrapper for search result item
 *
 */
public class TwitterRestSearchResponseItem extends TwitterRestResponseItem implements RestResponsetServiceItem{

	String id;
	
	public TwitterRestSearchResponseItem(String text, Date date, String id) {
		super(text, date);
		this.id = id;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	

}
