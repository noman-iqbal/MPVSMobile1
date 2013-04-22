package com.mpvs.mpvsmobile.db;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ViewResultDB extends MPVS_DB {

	public static final String TABLE_NAME = "student_result";
	public static final String EXAM_ID = "exam_id";
	public static final String EXAM_TITLE = "exam_title";
	public static final String EXAM_DEC = "exam_dec";
	public static final String EXAM_CONTENT = "content";
	public static final String VIEW_RESULT_Q = "CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY," + EXAM_ID + " TEXT," + EXAM_TITLE + " TEXT," + EXAM_DEC + " TEXT," + EXAM_CONTENT + " TEXT);";

	public ViewResultDB(Context context) {
		super(context);
	}

	public void add(JSONObject obj) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, obj.optString("exam_id"));
		v.put(EXAM_TITLE, obj.optString("exam_title"));
		v.put(EXAM_DEC, obj.optString("exam_dec"));
		v.put(EXAM_CONTENT, obj.optJSONArray("contents").toString());
		database.insert(TABLE_NAME, null, v);
		database.close();
	}
	
	
	
	public Cursor  getAll() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor c = database.query(TABLE_NAME, new String[]{EXAM_TITLE,EXAM_DEC,EXAM_CONTENT}, null,null, null, null, null);
		return c;
	}
	

}
