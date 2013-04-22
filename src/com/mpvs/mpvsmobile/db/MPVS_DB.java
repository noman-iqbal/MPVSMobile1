package com.mpvs.mpvsmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MPVS_DB extends SQLiteOpenHelper {
	public MPVS_DB(Context context) {
		super(context, "mpvss.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL(ExamsDb.createExamQuery);
		arg0.execSQL(StudentsDB.CREATE_TABEL_Q);
		arg0.execSQL(ResultsDb.CREATE_TABLE_Q);
		arg0.execSQL(ExamContentDb.EXAM_CONTENT_QUERY);
		arg0.execSQL(ViewResultDB.VIEW_RESULT_Q);
		arg0.execSQL(MessagesDb.MESSAGE_Q);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public void delet() {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(ExamsDb.TableName, null, null);
		database.delete(StudentsDB.TABEL_NAME, null, null);
		database.delete(ResultsDb.TABLE_NAME, null, null);
		database.delete(ExamContentDb.TABEL_NAME, null, null);
		database.delete(ViewResultDB.TABLE_NAME, null, null);
		database.delete(MessagesDb.TABLE_NAME, null, null);
	}

}
