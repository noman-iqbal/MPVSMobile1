package com.mpvs.mpvsmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StudentsDB extends MPVS_DB {

	public static String TABEL_NAME = "students";
	public static String EXAM_ID = "exam_id";
	public static String STUDENT_ID = "student_id";
	public static String STUDENT_NAME = "student_name";
	public static String STUDENT_USERNAME = "student_username";
	public static String STATUS = "status";

	public static final String CREATE_TABEL_Q = "CREATE TABLE " + TABEL_NAME + "( _id INTEGER PRIMARY KEY," + EXAM_ID + " TEXT ," + STUDENT_ID + " TEXT," + STUDENT_NAME + " TEXT," + STUDENT_USERNAME + " TEXT," + STATUS + " TEXT);";

	public StudentsDB(Context context) {
		super(context);
	}

	public void addStudent(Student obj) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, obj.examId);
		v.put(STUDENT_ID, obj.studentId);
		v.put(STUDENT_NAME, obj.studentName);
		v.put(STUDENT_USERNAME, obj.studentUserName);
		v.put(STATUS, obj.status);
		database.insert(TABEL_NAME, null, v);
		database.close();
	}

	public Cursor getStudents(String examId) {
		if (examId == null) {
			examId = "35";
		}
		SQLiteDatabase database = getReadableDatabase();
		Cursor c = database.query(TABEL_NAME, new String[] { "_id", EXAM_ID, STUDENT_ID, STUDENT_NAME, STUDENT_USERNAME ,STATUS}, "exam_id=?", new String[] { examId }, null, null, null);
		return c;
	}
	
	
	public Cursor getStudent(String examId,String stId) {
		if (examId == null) {
			examId = "35";
		}
		SQLiteDatabase database = getReadableDatabase();
		Cursor c = database.query(TABEL_NAME, new String[] { "_id", EXAM_ID, STUDENT_ID, STUDENT_NAME, STUDENT_USERNAME,STATUS }, "exam_id=? and "+STUDENT_ID+" =?", new String[] { examId ,stId}, null, null, null);
		return c;
	}

	public void updateStudent(Student sObj) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(EXAM_ID, sObj.examId);
		v.put(STUDENT_ID, sObj.studentId);
		v.put(STUDENT_NAME, sObj.studentName);
		v.put(STUDENT_USERNAME, sObj.studentUserName);
		v.put(STATUS, sObj.status);
		database.update(TABEL_NAME, v, EXAM_ID + "=? and "+STUDENT_ID+"=?", new String[] { sObj.examId ,sObj.studentId});
		database.close();
	}
}
