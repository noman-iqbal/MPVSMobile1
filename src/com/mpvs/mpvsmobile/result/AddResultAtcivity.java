package com.mpvs.mpvsmobile.result;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpvs.mpvsmobile.CommonUtilities;
import com.mpvs.mpvsmobile.R;
import com.mpvs.mpvsmobile.db.ExamContent;
import com.mpvs.mpvsmobile.db.ExamContentDb;
import com.mpvs.mpvsmobile.db.Result;
import com.mpvs.mpvsmobile.db.ResultsDb;
import com.mpvs.mpvsmobile.db.Student;
import com.mpvs.mpvsmobile.db.StudentsDB;

public class AddResultAtcivity extends Activity {
	private String studentId;
	private LinearLayout layoutView;
	private Bundle b;
	private String status;
	private ResultsDb rdb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_page);
		
		layoutView = (LinearLayout) findViewById(R.id.result_content);
		rdb = new ResultsDb(this);
		b = getIntent().getExtras();
		studentId = b.getString("studentId");
		getActionBar().setTitle(b.getString("studentName"));
		status = b.getString("status");
		ExamContentDb ec = new ExamContentDb(this);
		if (status.contains("draft")) {
			Cursor c = ec.getContent(b.getString("examId"));

			while (c.moveToNext()) {
				String lable = c.getString(c.getColumnIndex(ExamContentDb.EXAM_C_NAME));
				String max = c.getString(c.getColumnIndex(ExamContentDb.EXAM_C_PERCENTAGE));
				String id = c.getString(c.getColumnIndex(ExamContentDb.EXAM_C_ID));
				View vie = getResultFeild(lable, max, "", id);

				layoutView.addView(vie);
			}

		} else if (status.contains("update")) {
			Cursor c = rdb.getBy(studentId, b.getString("examId"));
			while (c.moveToNext()) {
				String contentId = c.getString(c.getColumnIndex(ResultsDb.CONTENT_ID));
				String result = c.getString(c.getColumnIndex(ResultsDb.RESULT));
				ExamContent s = ec.getContentById(contentId);
				View vie = getResultFeild(s.examCName, s.examCPercentage, result, contentId);
				layoutView.addView(vie);
			}
		}

	}

	public View getResultFeild(String lable, String max, String defVal, String cId) {
		View v = LayoutInflater.from(this).inflate(R.layout.result_conent_row, null);
		TextView l = (TextView) v.findViewById(R.id.c_name);
		EditText e = (EditText) v.findViewById(R.id.result_content);
		l.setText(lable);
		if (!defVal.isEmpty()) {
			e.setText(defVal);
		}
		e.setHint(max);
		e.setTag(cId);
		e.setFilters(new InputFilter[] { new InputFilterMinMax("0", max) });
		return v;
	}

	public void onSave(View v) {
	

		StudentsDB sdb = new StudentsDB(this);
		Cursor student = sdb.getStudent(b.getString("examId"), studentId);
		student.moveToNext();
		Student s = new Student();
		s.examId = student.getString(student.getColumnIndex(StudentsDB.EXAM_ID));
		s.status = student.getString(student.getColumnIndex(StudentsDB.STATUS));
		s.studentId = student.getString(student.getColumnIndex(StudentsDB.STUDENT_ID));
		s.studentName = student.getString(student.getColumnIndex(StudentsDB.STUDENT_NAME));
		s.studentUserName = student.getString(student.getColumnIndex(StudentsDB.STUDENT_USERNAME));
		s.status = "update";
		sdb.updateStudent(s);

		int count = layoutView.getChildCount();
		for (int i = 0; i < count; i++) {
			LinearLayout layout = (LinearLayout) layoutView.getChildAt(i);
			EditText editText = (EditText) layout.getChildAt(1);
			CommonUtilities.logcat(editText.getText().toString());
			Result r = new Result();
			r.examId = b.getString("examId");
			r.studentId = studentId;
			r.contentId = (String) editText.getTag();
			r.result = editText.getText().toString();
			r.status = "draft";
			if (status.contains("update")) {
				rdb.update(r);
			} else if (status.contains("draft")) {
				rdb.add(r);
			}
		}
		Intent j = new Intent(this, UploadResultService.class);
		startService(j);

	}

}
