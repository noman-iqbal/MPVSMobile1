package com.mpvs.mpvsmobile.api;

import static com.mpvs.mpvsmobile.CommonUtilities.PREFERENCE;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mpvs.mpvsmobile.CommonUtilities;

public final class MPVSUser {
	public static String userName;
	public static String password;
	public static String isStudent;
	public static String id;

	static String paramUserName = "user_name";
	static String paramPassword = "password";
	static String paramId = "id";
	static String paramIsStudent = "isStudent";

	public static Boolean save(Context c) {
		CommonUtilities.logcat("Save");
		Editor editor = c.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit();
		editor.putString(paramId, id);
		editor.putString(paramUserName, userName);
		editor.putString(paramPassword, password);
		editor.putString(paramIsStudent, isStudent);

		return editor.commit();
	}

	public static Boolean load(Context c) {
		CommonUtilities.logcat("Load");
		SharedPreferences sp = c.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

		if (!sp.contains(paramId)) {
			return false;
		}
		id = sp.getString(paramId, paramId);
		isStudent = sp.getString(paramIsStudent, paramIsStudent);
		userName = sp.getString(paramUserName, paramUserName);
		password = sp.getString(paramPassword, paramPassword);
		return true;
	}

	public static Boolean clear(Context c) {
		CommonUtilities.logcat("Clear");
		Editor editor = c.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit();
		editor.clear();
		return editor.commit();
	}

}
