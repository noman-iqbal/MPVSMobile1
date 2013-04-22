package com.mpvs.mpvsmobile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

	/**
	 * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
	 */
	public static final String SERVER_URL = "http://192.168.0.102:8055/MPVS";

	/**
	 * Google API project id registered to use GCM.
	 */
	public static final String SENDER_ID = "264226771854";

	/**
	 * Tag used on log messages.
	 */
	public static final String TAG = "MPVS";
	/**
	 * Tag used on log messages.
	 */
	public static final String PREFERENCE = "MPVS_PREFERENCE";

	/**
	 * Intent used to display a message in the screen.
	 */
	public static final String GCM_MESSAGE_ACTION = "com.mpvs.mpvsmobile.DISPLAY_MESSAGE";

	/**
	 * Intent's extra that contains the message to be displayed.
	 */
	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */

	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(GCM_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	public static void logcat(String message) {
		Log.e(TAG, message);
	}

	public static void alert(Context c, String message) {
		Toast.makeText(c, TAG+"-"+message, Toast.LENGTH_SHORT).show();
	}
}
