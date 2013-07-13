package code.java.restya.core;

import java.util.Date;

/**
 * general purpose rest response item implementation
 *
 */
public abstract class BasicRestResponseItem {

	public BasicRestResponseItem(String text, Date date) {
		super();
		this.text = text;
		this.date = date;
	}

	protected String text;
	
	protected Date date;
	
}
