package com.icloud.cronin.peter.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.icloud.cronin.peter.model.RaceCourse;

public class RaceCourseDBDataSource extends DataSource<RaceCourse>{
	
public static final String TABLE_RACE_COURSE = "RACE_COURSE";
	
	public static final String COLUMN_COURSE = "COURSE";
	public static final String COLUMN_COURSE_NUMBER = "COURSE_NUMBER";

	public RaceCourseDBDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean insert(RaceCourse entity) {
		if (entity == null) {
			return false;
		}
		long result = mDatabase.insert(TABLE_RACE_COURSE, null,
				generateContentValuesFromObject(entity));
		return result != -1;
	}

	@Override
	public boolean delete(RaceCourse entity) {
		if (entity == null) {
			return false;
		}
		int result = mDatabase.delete(TABLE_RACE_COURSE,
				COLUMN_COURSE_NUMBER + " = '" + entity.getCourseNumber()+"'", null);
		return result != 0;
	}

	@Override
	public boolean update(RaceCourse entity) {
		if (entity == null) {
			return false;
		}
		int result = mDatabase.update(TABLE_RACE_COURSE,
				generateContentValuesFromObject(entity), COLUMN_COURSE_NUMBER + " = "
						+ entity.getCourseNumber(), null);
		return result != 0;
	}

	@Override
	public List<RaceCourse> read() {
		Cursor cursor = mDatabase.query(TABLE_RACE_COURSE, getAllColumns(), null,
				null, null, null, null);
		List<RaceCourse> courseList = new ArrayList<RaceCourse>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				courseList.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return courseList;
	}

	@Override
	public List<RaceCourse> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor cursor = mDatabase.query(TABLE_RACE_COURSE, getAllColumns(), selection,
				selectionArgs, groupBy, having, orderBy);
		List<RaceCourse> courseList = new ArrayList<RaceCourse>();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				courseList.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return courseList;
	}

	public String[] getAllColumns() {
		return new String[] {COLUMN_COURSE,COLUMN_COURSE_NUMBER};
	}

	public RaceCourse generateObjectFromCursor(Cursor cursor) {
		if (cursor == null) {
			return null;
		}
		RaceCourse race = new RaceCourse();
		race.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
		race.setCourseNumber(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NUMBER)));
		return race;
	}

	public ContentValues generateContentValuesFromObject(RaceCourse entity) {
		if (entity == null) {
			return null;
		}
		ContentValues values = new ContentValues();
		values.put(COLUMN_COURSE, entity.getCourse());
		values.put(COLUMN_COURSE_NUMBER, entity.getCourseNumber());
		return values;
	}

	@Override
	public List<RaceCourse> readSpecificColumns(String tableName,
			String[] selectColums, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		return null;
	}
}
