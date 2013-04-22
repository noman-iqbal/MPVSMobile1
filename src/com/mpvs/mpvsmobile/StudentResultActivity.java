package com.mpvs.mpvsmobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mpvs.mpvsmobile.db.ViewResultDB;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentResultActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_result_page);
		LinearLayout ll=(LinearLayout) findViewById(R.id.cnt);
		ViewResultDB vr=new ViewResultDB(this);
		Cursor c = vr.getAll();
		while (c.moveToNext()) {
			ll.addView(getRow(c));
		}
	}

	public View getContent(JSONObject obj) {
		View v = LayoutInflater.from(this).inflate(R.layout.view_result_content, null);
		TextView lable = (TextView) v.findViewById(R.id.lable);
		TextView mark = (TextView) v.findViewById(R.id.marks);
		lable.setText(obj.optString("title"));
		mark.setText(obj.optString("marks"));
		return v;
	}

	public View getRow(Cursor c) {
		View v = LayoutInflater.from(this).inflate(R.layout.view_result_row, null);
		TextView title = (TextView) v.findViewById(R.id.title);
		TextView dec = (TextView) v.findViewById(R.id.dec);
		LinearLayout contents = (LinearLayout) v.findViewById(R.id.contents);
		title.setText(c.getString(c.getColumnIndex(ViewResultDB.EXAM_TITLE)));
		dec.setText(c.getString(c.getColumnIndex(ViewResultDB.EXAM_DEC)));
		try {
			JSONArray content = new JSONArray(c.getString(c.getColumnIndex(ViewResultDB.EXAM_CONTENT)));
			for (int i = 0; i < content.length(); i++) {
				contents.addView(getContent(content.optJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return v;
	}
}
