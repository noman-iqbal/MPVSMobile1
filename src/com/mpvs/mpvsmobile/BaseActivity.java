package com.mpvs.mpvsmobile;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.mpvs.mpvsmobile.db.ExamsDb;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	Fragment mContent = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Exams");
		setBehindContentView(R.layout.menu_frame);
		setContentView(R.layout.content_frame);
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setMode(SlidingMenu.RIGHT);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new StudentListFragment();

		ExamListFragment mFrag = new ExamListFragment();

		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		t.replace(R.id.content_frame, mFrag);
		t.commit();

		ExamsDb ex = new ExamsDb(this);
		Cursor c = ex.getExam();
		c.moveToNext();
		String examId = c.getString(c.getColumnIndex(ExamsDb.examId));
		Bundle b = new Bundle();
		b.putString("examId", examId);
		mContent.setArguments(b);

		FragmentTransaction t1 = this.getSupportFragmentManager().beginTransaction();
		t1.replace(R.id.menu_frame, mContent);
		t1.commit();

		setSlidingActionBarEnabled(false);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, fragment).commit();
		getSlidingMenu().showMenu();
	}

}
