package code.java.restya.core;

/**
 * basic implementation for a rest response
 *
 */
public abstract class BasicRestResponse {
	
	
	public BasicRestResponse(boolean isSuccess, String errorMsg, int length,
			boolean hasMore) {
		super();
		this.isSuccess = isSuccess;
		this.errorMsg = errorMsg;
		this.length = length;
		this.hasMore = hasMore;
	}

	boolean isSuccess ;
	String errorMsg;
	int length ;
	boolean hasMore;
	
	public boolean isSuccess() {
		// TODO Auto-generated method stub
		return isSuccess;
	}
	
	protected void setSuccess(boolean success){
		isSuccess = success;
	}


	public String getError() {
		// TODO Auto-generated method stub
		return errorMsg;
	}

	protected void setLength(int len){
		length =len;
	}
	
	public int length() {
	
		return length;
	}

	
	public boolean hasMore() {
	
		return hasMore;
	}
	
	protected void setMore(boolean hasMore){
		this.hasMore = hasMore;
	}

}
