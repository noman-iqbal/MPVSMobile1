package com.mpvs.mpvsmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExamContentDb extends MPVS_DB {

	public static final String EXAM_ID = "exam_id";
	public static final String EXAM_C_NAME = "exam_c_name";
	public static final String EXAM_C_PERCENTAGE = "exam_c_percentage";
	public static final String EXAM_C_ID = "exam_c_id";
	public static final String TABEL_NAME = "exam_content";

	public static final String EXAM_CONTENT_QUERY = " CREATE TABLE " + TABEL_NAME + " (_id INTEGER PRIMARY KEY," + EXAM_ID + " TEXT," + EXAM_C_ID + " TEXT," + EXAM_C_NAME + " TEXT," + EXAM_C_PERCENTAGE + " TEXT);";

	public ExamContentDb(Context context) {
		super(context);
	}

	public void addContent(ExamContent ec) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, ec.examId);
		v.put(EXAM_C_ID, ec.examCId);
		v.put(EXAM_C_NAME, ec.examCName);
		v.put(EXAM_C_PERCENTAGE, ec.examCPercentage);
		database.insert(TABEL_NAME, null, v);
		database.close();
	}

	public Cursor getContent(String examId) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor c = database.query(TABEL_NAME, new String[] { "_id", EXAM_ID, EXAM_C_ID, EXAM_C_NAME, EXAM_C_PERCENTAGE }, EXAM_ID + "=?", new String[] { examId }, null, null, null);
		database.close();
		return c;
	}

	public void updateContent(ExamContent sObj) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, sObj.examId);
		v.put(EXAM_C_ID, sObj.examCId);
		v.put(EXAM_C_NAME, sObj.examCName);
		v.put(EXAM_C_PERCENTAGE, sObj.examCPercentage);
		database.update(TABEL_NAME, v, EXAM_ID + "=?", new String[] { sObj.examId });
		database.close();

	}

	public ExamContent getContentById(String contentId) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor c = database.query(TABEL_NAME, new String[] { "_id", EXAM_C_NAME, EXAM_C_PERCENTAGE }, EXAM_C_ID + "=?", new String[] { contentId }, null, null, null);
		c.moveToNext();
		ExamContent ec = new ExamContent();
		ec.examCName = c.getString(c.getColumnIndex(EXAM_C_NAME));
		ec.examCPercentage = c.getString(c.getColumnIndex(EXAM_C_PERCENTAGE));
		database.close();
		return ec;

	}

}