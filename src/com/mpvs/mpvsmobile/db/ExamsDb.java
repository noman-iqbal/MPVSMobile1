package com.mpvs.mpvsmobile.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExamsDb extends MPVS_DB {
	public static String id = "_id";
	public static String examId = "exam_id";
	public static String title = "title";
	public static String createdAt = "created_at";
	public static String updatedAt = "updated_at";
	public static String description = "description";
	public static String subjectName = "sb_name";
	public static String userCreaterId = "user_creater_id";
	public static String START_TIME = "start";
	public static String END_TIME = "end";
	public static String status = "status";
	public static String TableName = "exams";

	public static String createExamQuery = "CREATE TABLE " + TableName 
			+ "( " + id + " INTEGER PRIMARY KEY," 
			+ examId + " INTEGER," + title + " TEXT," 
			+ description + " TEXT ," + subjectName 
			+ " TEXT ," 
			+ START_TIME + " TEXT ," 
			+ END_TIME + " TEXT ," 
			+ status + " TEXT  );";

	public ExamsDb(Context context) {
		super(context);
	}

	public void addExam(Exam ex) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ExamsDb.examId, Integer.parseInt(ex.getId()));
		values.put(ExamsDb.title, ex.getTitle());
		values.put(ExamsDb.description, ex.getDescription());
		values.put(ExamsDb.status, ex.getStatus());
		values.put(ExamsDb.subjectName, ex.getSubjectName());
		values.put(ExamsDb.START_TIME, ex.getStart());
		values.put(ExamsDb.END_TIME, ex.getEnd());
		database.insert(ExamsDb.TableName, null, values);
		database.close();

	}

	public void addAllExam(List<Exam> exams) {
		SQLiteDatabase database = this.getWritableDatabase();

		for (Exam ex : exams) {
			ContentValues values = new ContentValues();
			values.put(ExamsDb.examId, ex.getId());
			values.put(ExamsDb.title, ex.getTitle());
			values.put(ExamsDb.description, ex.getDescription());
			values.put(ExamsDb.status, ex.getStatus());
			values.put(ExamsDb.subjectName, ex.getSubjectName());
			values.put(ExamsDb.START_TIME, ex.getStart());
			values.put(ExamsDb.END_TIME, ex.getEnd());

			database.insert(ExamsDb.TableName, null, values);
		}

		database.close();

	}

	public Boolean hasExam(Exam ex) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor c = database.query(ExamsDb.TableName, new String[] { ExamsDb.id, ExamsDb.title }, "exam_id=?", new String[] { ex.getId() }, null, null, null);
		return c.getCount() > 0;
	}

	public Cursor getExam() {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor c = database.query(ExamsDb.TableName, new String[] { id, examId, title, description,START_TIME,END_TIME }, null, null, null, null, null);
		return c;
	}

	public void updateExam(Exam examOb) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(ExamsDb.examId, examOb.getId());
		v.put(ExamsDb.title, examOb.getTitle());
		v.put(ExamsDb.description, examOb.getDescription());
		v.put(ExamsDb.status, examOb.getStatus());
		v.put(ExamsDb.subjectName, examOb.getSubjectName());
		v.put(ExamsDb.START_TIME, examOb.getStart());
		v.put(ExamsDb.END_TIME, examOb.getEnd());
		database.update(TableName, v, examId + "=?", new String[] { examOb.getId() });
		database.close();

	}
}
