package com.mpvs.mpvsmobile.db;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessagesDb extends MPVS_DB {

	public static final String TABLE_NAME = "messages";
	public static final String FROM_ID = "from_id";
	public static final String FROM_NAME = "name";
	public static final String DATE= "date";
	public static final String SUBJECT = "subject";
	public static final String MESSAGE = "message";
	public static String MESSAGE_Q = "CREATE TABLE " + TABLE_NAME + "( _id INTEGER PRIMARY KEY," + FROM_ID + " TEXT," + FROM_NAME + " TEXT," + SUBJECT + " TEXT," + DATE + " TEXT," + MESSAGE + " TEXT);";

	public MessagesDb(Context context) {
		super(context);
	}

	public void add(JSONObject obj) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(FROM_ID, obj.optString("from_id"));
		v.put(FROM_NAME, obj.optString("from_name"));
		v.put(DATE, obj.optString("date"));
		v.put(SUBJECT, obj.optString("subject"));
		v.put(MESSAGE, obj.optString("message"));
		db.insert(TABLE_NAME, null, v);
		db.close();

	}

	public Cursor getAll() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor c = database.query(TABLE_NAME, new String[] { "_id", FROM_ID, FROM_NAME, SUBJECT, MESSAGE ,DATE}, null, null, null, null, null);
	
		return c;
	}
}
