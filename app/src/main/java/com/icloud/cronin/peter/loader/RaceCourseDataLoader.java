package com.icloud.cronin.peter.loader;

import java.util.List;

import android.content.Context;

import com.icloud.cronin.peter.datasource.DataSource;
import com.icloud.cronin.peter.datasource.RaceCourseDBDataSource;
import com.icloud.cronin.peter.model.RaceCourse;

public class RaceCourseDataLoader extends DataLoader<List<RaceCourse>>{
	private DataSource<RaceCourse> mDataSource;
	private String mSelection;
	private String[] mSelectionArgs;
	private String mGroupBy;
	private String mHaving;
	private String mOrderBy;

	public RaceCourseDataLoader(Context context, RaceCourseDBDataSource mDataSource2, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		super(context);
		mDataSource = mDataSource2;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mGroupBy = groupBy;
		mHaving = having;
		mOrderBy = orderBy;
	}

	@Override
	protected List<RaceCourse> buildList() {
		List<RaceCourse> raceList = mDataSource.read(mSelection, mSelectionArgs, mGroupBy, mHaving, mOrderBy);
		return raceList;
	}
	
	public void insert(RaceCourse entity) {
		new InsertTask(this).execute(entity);
	}

	public void update(RaceCourse entity) {
		new UpdateTask(this).execute(entity);
	}

	public void delete(RaceCourse entity) {
		new DeleteTask(this).execute(entity);
	}

	private class InsertTask extends ContentChangingTask<RaceCourse, Void, Void> {
		InsertTask(RaceCourseDataLoader loader) {
			super(loader);
		}

		@Override
		protected Void doInBackground(RaceCourse... params) {
			mDataSource.insert(params[0]);
			return (null);
		}
	}

	private class UpdateTask extends ContentChangingTask<RaceCourse, Void, Void> {
		UpdateTask(RaceCourseDataLoader loader) {
			super(loader);
		}

		@Override
		protected Void doInBackground(RaceCourse... params) {
			mDataSource.update(params[0]);
			return (null);
		}
	}

	private class DeleteTask extends ContentChangingTask<RaceCourse, Void, Void> {
		DeleteTask(RaceCourseDataLoader loader) {
			super(loader);
		}

		@Override
		protected Void doInBackground(RaceCourse... params) {
			mDataSource.delete(params[0]);
			return (null);
		}
	}


}
