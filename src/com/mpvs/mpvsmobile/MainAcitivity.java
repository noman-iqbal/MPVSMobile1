package com.mpvs.mpvsmobile;

import com.mpvs.mpvsmobile.api.MPVSUser;
import com.mpvs.mpvsmobile.db.MPVS_DB;
import com.mpvs.mpvsmobile.db.ViewResultDB;
import com.mpvs.mpvsmobile.result.UploadResultService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainAcitivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		if (MPVSUser.isStudent.contains("member")) {
			findViewById(R.id.st).setVisibility(View.GONE);
		}

		if (MPVSUser.isStudent.contains("student")) {
			findViewById(R.id.ex).setVisibility(View.GONE);
		}

	}

	public void onExam(View v) {

		if (MPVSUser.isStudent.contains("member")) {

			Intent i = new Intent(this, BaseActivity.class);
			startActivity(i);
		} else {
			CommonUtilities.alert(this, "you are not member");
		}
	}

	public void onStudent(View v) {
		if (MPVSUser.isStudent.contains("student")) {
			Intent i = new Intent(this, StudentResultActivity.class);
			startActivity(i);
		} else {
			CommonUtilities.alert(this, "you are not student");
		}
	}

	public void onMessage(View v) {
		Intent i = new Intent(this, MessagesList.class);
		startActivity(i);
	}

	public void onLogout(View v) {
		MPVSUser.clear(this);
		Intent i = new Intent(this, UploadResultService.class);
		i.putExtra("logout", true);
		startService(i);
		MPVS_DB db=new MPVS_DB(this);
		db.delet();
		i = new Intent(this, LoginActivity.class);
		i.addCategory(Intent.CATEGORY_HOME);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

}
