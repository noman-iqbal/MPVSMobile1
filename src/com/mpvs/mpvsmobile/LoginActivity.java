package com.mpvs.mpvsmobile;

import static com.mpvs.mpvsmobile.CommonUtilities.GCM_MESSAGE_ACTION;
import static com.mpvs.mpvsmobile.CommonUtilities.SENDER_ID;
import static com.mpvs.mpvsmobile.api.MPVSUser.password;
import static com.mpvs.mpvsmobile.api.MPVSUser.userName;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity {
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			if (b != null) {
				String m = (String) b.get(CommonUtilities.EXTRA_MESSAGE);
				if (m.contains("login")) {
					intent = new Intent(context, MainAcitivity.class);
					startActivity(intent);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		registerReceiver(receiver, new IntentFilter(GCM_MESSAGE_ACTION));
	}

	public void onLogin(View v) {
		userName = ((TextView) findViewById(R.id.user_name)).getText().toString();
		password = ((TextView) findViewById(R.id.password)).getText().toString();
		if (userName.isEmpty() || password.isEmpty()) {
			CommonUtilities.alert(this, "some this is not right");
		}
		CommonUtilities.logcat("clicked");

		CommonUtilities.logcat(GCMRegistrar.getRegistrationId(this) + "asdf");

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		GCMRegistrar.register(this, SENDER_ID);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
