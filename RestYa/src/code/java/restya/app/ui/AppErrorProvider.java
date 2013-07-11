package code.java.restya.app.ui;

import android.content.Context;
import code.java.restya.core.ErrorCodes;

/**
 * General purpose application error UI handling
 *
 */
public interface AppErrorProvider {
   public void showError(Context ctx,ErrorCodes erorCodes);
   public void showError(Context ctx,String error);
}
