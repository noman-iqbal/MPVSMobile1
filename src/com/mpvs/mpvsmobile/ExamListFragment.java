package com.mpvs.mpvsmobile;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mpvs.mpvsmobile.db.ExamsDb;

public class ExamListFragment extends ListFragment {

	
	private ExamsDb ex;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] columns = new String[] { ExamsDb.title ,ExamsDb.START_TIME,ExamsDb.END_TIME};
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.row_title ,R.id.start,R.id.end};
		ex=new ExamsDb(getActivity());
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this.getActivity(), R.layout.row, ex.getExam(), columns, to);
		setListAdapter(sca);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = (Cursor) l.getAdapter().getItem(position);
		StudentListFragment st = new StudentListFragment();
		Bundle b = new Bundle();
		b.putString("examId", c.getString(c.getColumnIndex(ExamsDb.examId)));
		st.setArguments(b);
		switchFragment(st);
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof BaseActivity) {
			BaseActivity fca = (BaseActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

	

}
