package code.java.restya.providers.twitter;

import java.util.Date;

import code.java.restya.core.BasicRestResponseItem;

/**
 * a common implementation for all twitter response items
 *
 */
abstract class TwitterRestResponseItem extends BasicRestResponseItem {

	
	
	public TwitterRestResponseItem(String text, Date date) {
		super(text, date);
		
	}

}
