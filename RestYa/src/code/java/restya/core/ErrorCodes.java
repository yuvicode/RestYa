package code.java.restya.core;

import android.content.Context;
import code.java.restya.R;

/**
 * application error codes and their associated res codes
 *
 */
public enum ErrorCodes {
	
	SERVICE_UNAVAILABLE(R.string.err_service_unavailable),
	SERVICE_UNAUTHORIZED (R.string.err_not_auth),
	ERR_UNKNOWN (R.string.err_unknown),
	ERR_NO_CONNECTION (R.string.err_no_connection),
	ERR_PAGE_CANT_DISPLAY (R.string.err_no_page),
	SERVICE_NO_RESULTS(R.string.err_no_results),
	USER_ABORT(R.string.err_user_abort);
	
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
