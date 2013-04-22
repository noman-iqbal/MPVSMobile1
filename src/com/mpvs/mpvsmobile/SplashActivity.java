package com.mpvs.mpvsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mpvs.mpvsmobile.api.MPVSUser;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i;
		if (MPVSUser.load(this)) {
			i = new Intent(this, MainAcitivity.class);
		} else {
			i = new Intent(this, LoginActivity.class);
		}
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

}
