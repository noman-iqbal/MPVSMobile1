package com.mpvs.mpvsmobile;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.mpvs.mpvsmobile.db.MessagesDb;

public class MessagesList extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		getActionBar().setTitle("Messages");

		MessagesDb ms = new MessagesDb(this);
		String[] columns = new String[] { MessagesDb.FROM_NAME, MessagesDb.SUBJECT, MessagesDb.MESSAGE ,MessagesDb.DATE};
		int[] to = new int[] { R.id.from, R.id.subject, R.id.message ,R.id.date};
		Cursor c = ms.getAll();
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.row_maessage, c, columns, to);
		setListAdapter(sca);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

	}
}
