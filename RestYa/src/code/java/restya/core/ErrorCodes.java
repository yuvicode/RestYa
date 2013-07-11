package code.java.restya.core;

import code.java.restya.R;
import android.content.Context;

/**
 * application error codes and their associated res codes
 *
 */
public enum ErrorCodes {
	
	SERVICE_UNAVAILABLE(R.string.err_service_unavailable),
	SERVICE_UNAUTHORIZED (R.string.err_not_auth),
	ERR_UNKNOWN (R.string.err_unknown),
	ERR_NO_CONNECTION (R.string.err_no_connection),
	SERVICE_NO_RESULTS(R.string.err_no_results);
	
	
	int mErrResId;
	
	ErrorCodes(int errResId){
		mErrResId = errResId;
	}
	
	public int getErrResCode(){
		return mErrResId;
	}
	
	public String getResCodeString(Context ctx){
		return ctx.getResources().getString(mErrResId);
	}
	
}
