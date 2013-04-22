package com.mpvs.mpvsmobile.result;

import org.json.JSONObject;

import com.mpvs.mpvsmobile.CommonUtilities;
import com.mpvs.mpvsmobile.api.MPVSUser;
import com.mpvs.mpvsmobile.api.NetworkUtilities;
import com.mpvs.mpvsmobile.api.ServerUtilities;
import com.mpvs.mpvsmobile.db.Result;
import com.mpvs.mpvsmobile.db.ResultsDb;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class UploadResultService extends IntentService {

	public UploadResultService() {
		super("uploadResult");
		CommonUtilities.logcat("error");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		CommonUtilities.logcat("error");
		Bundle b = intent.getExtras();
		if (b != null && b.containsKey("logout")) {
			ServerUtilities.unregister(getApplicationContext(), MPVSUser.id);
		}

		if (MPVSUser.load(getBaseContext())) {

			ResultsDb tdb = new ResultsDb(getBaseContext());
			Cursor c = tdb.getResultToUp();
			while (c.moveToNext()) {
				try {
					Result r = new Result();
					r.contentId = c.getString(c.getColumnIndex(ResultsDb.CONTENT_ID));
					r.result = c.getString(c.getColumnIndex(ResultsDb.RESULT));
					r.studentId = c.getString(c.getColumnIndex(ResultsDb.STUDENT_ID));
					r.examId = c.getString(c.getColumnIndex(ResultsDb.EXAM_ID));
					String rId = c.getString(c.getColumnIndex("_id"));
					Bundle param = new Bundle();
					param.putString("content_id", r.contentId);
					param.putString("result", r.result);
					param.putString("student_id", r.studentId);
					param.putString("exam_id", r.examId);
					JSONObject obj = NetworkUtilities.updateResult(param);
					if (obj.optBoolean("isError")) {
						CommonUtilities.logcat("error");
					} else {
						r.status = "update";
						tdb.update(r);
					}
				} catch (Exception e) {
					CommonUtilities.logcat(e.getMessage());
				}
			}

		}

	}

}
