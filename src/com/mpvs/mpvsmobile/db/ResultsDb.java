package com.mpvs.mpvsmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ResultsDb extends MPVS_DB {

	public static final String TABLE_NAME = "results";
	public static final String EXAM_ID = "examId";
	public static final String STUDENT_ID = "studentId";
	public static final String CONTENT_ID = "contentId";
	public static final String RESULT = "result";
	public static final String STATUS = "status";

	public static final String CREATE_TABLE_Q = "CREATE TABLE " + TABLE_NAME + "( _id INTEGER PRIMARY KEY," + EXAM_ID + " TEXT," + STUDENT_ID + " TEXT," + CONTENT_ID + " TEXT," + RESULT + " TEXT," + STATUS + " TEXT);";

	public ResultsDb(Context context) {
		super(context);
	}

	public void add(Result r) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, r.examId);
		v.put(STUDENT_ID, r.studentId);
		v.put(CONTENT_ID, r.contentId);
		v.put(RESULT, r.result);
		v.put(STATUS, r.status);
		database.insert(TABLE_NAME, null, v);
		database.close();
	}

	public Cursor getBy(String stId, String examId) {
		SQLiteDatabase database = getReadableDatabase();
		Cursor c = database.query(TABLE_NAME, new String[] { CONTENT_ID, RESULT }, STUDENT_ID + "=? and " + EXAM_ID + "=?", new String[] { stId, examId }, null, null, null);
	
		return c;
	}

	public void update(Result r) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, r.examId);
		v.put(STUDENT_ID, r.studentId);
		v.put(CONTENT_ID, r.contentId);
		v.put(RESULT, r.result);
		v.put(STATUS, r.status);
		database.update(TABLE_NAME, v, EXAM_ID + "=? and " + STUDENT_ID + "=? and " + CONTENT_ID + "=?", new String[] { r.examId, r.studentId, r.contentId });
		database.close();
	}
	
	
	
	
	

	public Cursor getResultToUp() {
		SQLiteDatabase database = getWritableDatabase();
		Cursor c = database.query(TABLE_NAME, new String[] {"_id", EXAM_ID, CONTENT_ID, STUDENT_ID, RESULT }, STATUS + "='draft'", null, null, null, null);
		return c;

	}

}
