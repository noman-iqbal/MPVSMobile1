package com.mpvs.mpvsmobile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mpvs.mpvsmobile.db.StudentsDB;
import com.mpvs.mpvsmobile.result.AddResultAtcivity;

public class StudentListFragment extends ListFragment {

	private StudentsDB st;
	private SimpleCursorAdapter sca;
	private Cursor c;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		st = new StudentsDB(getActivity());
		String[] columns = new String[] { StudentsDB.STUDENT_USERNAME };
		int[] to = new int[] { R.id.row_title };
		c = st.getStudents(getArguments().getString("examId"));
		sca = new SimpleCursorAdapter(this.getActivity(), R.layout.row, c, columns, to);
		setListAdapter(sca);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = (Cursor) l.getAdapter().getItem(position);
		Intent i = new Intent(getActivity(), AddResultAtcivity.class);
		i.putExtra("examId", c.getString(c.getColumnIndex(StudentsDB.EXAM_ID)));
		i.putExtra("status", c.getString(c.getColumnIndex(StudentsDB.STATUS)));
		i.putExtra("studentId", c.getString(c.getColumnIndex(StudentsDB.STUDENT_ID)));
		i.putExtra("studentName", c.getString(c.getColumnIndex(StudentsDB.STUDENT_NAME)));
		startActivityForResult(i,0);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityCreated(null);
	
	}
	
	
}
