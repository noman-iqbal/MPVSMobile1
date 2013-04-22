/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mpvs.mpvsmobile;

import static com.mpvs.mpvsmobile.CommonUtilities.SENDER_ID;
import static com.mpvs.mpvsmobile.CommonUtilities.displayMessage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.mpvs.mpvsmobile.api.MPVSUser;
import com.mpvs.mpvsmobile.api.NetworkUtilities;
import com.mpvs.mpvsmobile.api.ServerUtilities;
import com.mpvs.mpvsmobile.db.Exam;
import com.mpvs.mpvsmobile.db.ExamContent;
import com.mpvs.mpvsmobile.db.ExamContentDb;
import com.mpvs.mpvsmobile.db.ExamsDb;
import com.mpvs.mpvsmobile.db.MessagesDb;
import com.mpvs.mpvsmobile.db.Student;
import com.mpvs.mpvsmobile.db.StudentsDB;
import com.mpvs.mpvsmobile.db.ViewResultDB;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	@SuppressWarnings("hiding")
	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, "this is regesterd");
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		Log.i(TAG, intent.getExtras().toString());
		String message = intent.getExtras().getString("message");
		String opType = intent.getExtras().getString("operation_type");
		try {

			if (opType.equals("message")) {
				addMessage(context, NetworkUtilities.getMessages(intent.getExtras().getString("m_id")));
			}

			if (opType.equals("updateExam")) {
				String isUpdate = intent.getExtras().getString("isUpdate");
				MPVSUser.load(context);
				if (isUpdate.equals("true")) {
					updateExam(context, NetworkUtilities.getExam(intent.getExtras().getString("examId")));
				} else if (isUpdate.equals("false")) {
					addExams(context, NetworkUtilities.getExam(intent.getExtras().getString("examId")));
				}

			} else if (opType.equals("login")) {
				displayMessage(context, "login");

				MPVSUser.id = intent.getExtras().getString("id");
				MPVSUser.isStudent = intent.getExtras().getString("is_student");
				MPVSUser.save(context);
				addMessage(context, NetworkUtilities.getMessages());
				if (MPVSUser.isStudent.contains("student")) {
					addResult(context, NetworkUtilities.getAllResult());
				} else {
					addExams(context, NetworkUtilities.getAllExam());
				}
			} else if (opType.equals("updateResult")) {
				MPVSUser.load(context);
				String examId = intent.getExtras().getString("exam_id");
				addResult(context, NetworkUtilities.getResult(examId));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent = new Intent(CommonUtilities.GCM_MESSAGE_ACTION);
		intent.putExtra(CommonUtilities.EXTRA_MESSAGE, message);
		intent.putExtra("operation_type", opType);
		context.sendBroadcast(intent);
		// notifies user
		generateNotification(context, message, opType);
	}

	private void addMessage(Context context, JSONObject messages) {
		MessagesDb vr = new MessagesDb(context);
		JSONArray results = messages.optJSONArray("data");
		for (int i = 0; i < results.length(); i++) {
			vr.add(results.optJSONObject(i));
		}

	}

	private void addResult(Context context, JSONObject allResult) {
		ViewResultDB vr = new ViewResultDB(context);
		JSONArray results = allResult.optJSONArray("data");
		for (int i = 0; i < results.length(); i++) {
			vr.add(results.optJSONObject(i));
		}

	}

	private void updateExam(Context context, JSONObject exam) {
		ExamsDb ex = new ExamsDb(context);
		StudentsDB sb = new StudentsDB(context);
		ExamContentDb ec = new ExamContentDb(context);

		JSONArray exams = exam.optJSONArray("data");
		for (int i = 0; i < exams.length(); i++) {
			JSONObject json = (JSONObject) exams.opt(i);
			Exam examOb = new Exam();
			examOb.setId(json.optString("exam_id"));
			examOb.setTitle(json.optString("exam_title"));
			examOb.setDescription(json.optString("exam_description"));
			
			examOb.setStart(json.optString("exam_get_start_date"));
			examOb.setEnd(json.optString("exam_get_end_date"));
			
			examOb.setSubjectName(json.optString("exam_subject_name"));
			examOb.setStatus(json.optString("exam_status"));
			JSONArray student = json.optJSONArray("students");
			for (int j = 0; j < student.length(); j++) {
				JSONObject studentObj = (JSONObject) student.opt(j);
				Student sObj = new Student();
				sObj.examId = examOb.getId();
				sObj.studentId = studentObj.optString("s_id");
				sObj.studentName = studentObj.optString("s_name");
				sObj.studentUserName = studentObj.optString("s_user_name");
				sb.updateStudent(sObj);
			}
			JSONArray content = json.optJSONArray("exam_contents");
			for (int j = 0; j < content.length(); j++) {
				JSONObject contentObj = (JSONObject) content.opt(j);
				ExamContent sObj = new ExamContent();
				sObj.examId = examOb.getId();
				sObj.examCId = contentObj.optString("exam_content_id");
				sObj.examCName = contentObj.optString("exam_content_name");
				sObj.examCPercentage = contentObj.optString("exam_content_percentage");
				ec.updateContent(sObj);
			}
			ex.updateExam(examOb);

		}

	}

	private void addExams(Context context, JSONObject obj) {
		ExamsDb ex = new ExamsDb(context);
		StudentsDB sb = new StudentsDB(context);
		ExamContentDb ec = new ExamContentDb(context);

		JSONArray exams = obj.optJSONArray("data");
		List<Exam> list = new ArrayList<Exam>();
		for (int i = 0; i < exams.length(); i++) {
			JSONObject json = (JSONObject) exams.opt(i);
			Exam examOb = new Exam();
			examOb.setId(json.optString("exam_id"));
			examOb.setTitle(json.optString("exam_title"));
			examOb.setDescription(json.optString("exam_description"));
			examOb.setStart(json.optString("exam_get_start_date"));
			examOb.setEnd(json.optString("exam_get_end_date"));
			examOb.setSubjectName(json.optString("exam_subject_name"));
			examOb.setStatus(json.optString("exam_status"));
			JSONArray student = json.optJSONArray("students");
			for (int j = 0; j < student.length(); j++) {
				JSONObject studentObj = (JSONObject) student.opt(j);
				Student sObj = new Student();
				sObj.examId = examOb.getId();
				sObj.studentId = studentObj.optString("s_id");
				sObj.studentName = studentObj.optString("s_name");
				sObj.studentUserName = studentObj.optString("s_user_name");
				sb.addStudent(sObj);
			}
			JSONArray content = json.optJSONArray("exam_contents");
			for (int j = 0; j < content.length(); j++) {
				JSONObject contentObj = (JSONObject) content.opt(j);
				ExamContent sObj = new ExamContent();
				sObj.examId = examOb.getId();
				sObj.examCId = contentObj.optString("exam_content_id");
				sObj.examCName = contentObj.optString("exam_content_name");
				sObj.examCPercentage = contentObj.optString("exam_content_percentage");
				ec.addContent(sObj);
			}
			list.add(examOb);
		}
		ex.addAllExam(list);

	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 * 
	 * @param opType
	 */
	private static void generateNotification(Context context, String message, String opType) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = null;
		notificationIntent = new Intent(context, MainAcitivity.class);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

}
