package code.java.restya.app.ui;

import android.content.Context;
import android.widget.Toast;
import code.java.restya.core.ErrorCodes;

/**
 * Simple Toast base error presenter
 *
 */
public class SimpleToastErrorPresenter implements AppErrorProvider{

	@Override
	public void showError(Context ctx, ErrorCodes erorCodes) {
		Toast.makeText(ctx, erorCodes.getErrResCode(), Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void showError(Context ctx, String error) {
		Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
		
	}

}
