package code.java.restya.core;

import java.util.Date;

/**
 * a definition of a rest result item
 *
 */
public interface RestResponsetServiceItem {
	
	public String getFieldAsString(String fieldName);
	public Date getFieldAsDate(String fieldName);
	public int getFieldAsInt(String fieldName);

}
