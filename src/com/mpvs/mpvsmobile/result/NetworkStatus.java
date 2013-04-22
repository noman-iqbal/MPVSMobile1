package com.mpvs.mpvsmobile.result;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStatus extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("", "Network connectivity change");
		if (intent.getExtras() != null) {
			NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
				Intent i=new Intent(context,UploadResultService.class);
				context.startService(i);
			}
		}
		if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
			Log.d("", "There's no network connectivity");
		}
	}

}
