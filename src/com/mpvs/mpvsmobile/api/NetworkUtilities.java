package com.mpvs.mpvsmobile.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.mpvs.mpvsmobile.CommonUtilities;

public class NetworkUtilities {

	private static final String APP_BASE_URL = CommonUtilities.SERVER_URL;

	public static JSONObject getAllExam() throws JSONException, IOException, InterruptedException {

		Bundle parms = new Bundle();
		parms.putString("user_id", MPVSUser.id);
		parms.putString("function_name", "getAllExam");
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));
	}

	public static JSONObject getExam(String id) throws JSONException, IOException, InterruptedException {

		Bundle parms = new Bundle();
		parms.putString("user_id", MPVSUser.id);
		parms.putString("function_name", "getExam");
		parms.putString("exam_ids", id);
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));
	}

	public static JSONObject updateResult(Bundle result) throws JSONException, IOException, InterruptedException {
		Bundle parms = new Bundle();
		parms.putAll(result);
		parms.putString("user_id", MPVSUser.id);
		parms.putString("function_name", "upload");
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));

	}

	public static class Run extends AsyncTask<Object, Void, Object> {

		private NetworkListenr listener;

		@Override
		protected Object doInBackground(Object... params) {
			listener = (NetworkListenr) params[3];

			String url = getUrl((String) params[0], (Bundle) params[1]);
			Log.e("Request", url);
			JSONObject temp;
			try {
				temp = new JSONObject(getUrlContent(url));
			} catch (Exception e) {
				return e;
			}
			return temp;
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			Log.e("Result", result + "");

			if (result instanceof Exception) {
				listener.onError((Exception) result);
				return;
			}
			JSONObject temp = (JSONObject) result;
			if (temp.optString("errorCode").startsWith("E") || temp.optString("errorCode").startsWith("1")) {
				listener.onError(null);
				return;
			}
			listener.onComplete(temp);
		}
	}

	// Return complet URL with some added functions
	public static String getUrl(String call, Bundle parameters) {

		return APP_BASE_URL + "/" + call + "?" + encodeUrl(parameters);
	}

	private static String encodeUrl(Bundle parameters) {
		if (parameters == null)
			return "";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			if (first)
				first = false;
			else
				sb.append("&");
			try {
				sb.append(key + "=" + URLEncoder.encode(parameters.getString(key), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	public interface NetworkListenr {
		public void onComplete(JSONObject result);

		public void onError(Exception ex);

	}

	static AndroidHttpClient client;

	private static BasicHttpContext v = new BasicHttpContext();;
	private static BasicCookieStore cookieStore = new BasicCookieStore();
	private static int soTimeOut = 500000;
	private static int connectionTimeOut = 100000;

	public static String getUrlContent(String url) throws IOException, InterruptedException {

		v.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		client = AndroidHttpClient.newInstance("android");
		HttpConnectionParams.setConnectionTimeout(client.getParams(), connectionTimeOut);
		HttpConnectionParams.setSoTimeout(client.getParams(), soTimeOut);

		HttpGet a = new HttpGet(url);
		HttpEntity entry = null;
		entry = client.execute(a, v).getEntity();
		InputStream in = entry.getContent();
		StringBuilder sb = new StringBuilder();
		Log.e("string builder capacity", sb.capacity() + "");
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 100);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	private static void showBundle(Bundle myBundle2) {
		for (String key : myBundle2.keySet()) {
			Object data = myBundle2.get(key);
			if (data instanceof byte[]) {
				Log.e(key, data + "");
			} else {
				if (!((String) data).isEmpty()) {
					Log.e(key, data + "");
				}
			}
		}

	}

	public static JSONObject getAllResult() throws JSONException, IOException, InterruptedException {
		Bundle parms = new Bundle();
		parms.putString("user_id", MPVSUser.id);
		parms.putString("function_name", "getAllResult");
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));
	}

	public static JSONObject getResult(String examId) throws JSONException, IOException, InterruptedException {
		Bundle parms = new Bundle();
		parms.putString("user_id", MPVSUser.id);
		parms.putString("exam_id", examId);
		parms.putString("function_name", "getResult");
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));
	}

	public static JSONObject getMessages() throws JSONException, IOException, InterruptedException {
		Bundle parms = new Bundle();
		parms.putString("user_id", MPVSUser.id);
		parms.putString("function_name", "getMessages");
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));

	}

	public static JSONObject getMessages(String string) throws JSONException, IOException, InterruptedException {
		Bundle parms = new Bundle();
		parms.putString("user_id", MPVSUser.id);
		parms.putString("function_name", "getMessagesByid");
		parms.putString("m_id", string);
		String url = getUrl("examApi", parms);
		CommonUtilities.logcat(url);
		return new JSONObject(getUrlContent(url));
	}

}
