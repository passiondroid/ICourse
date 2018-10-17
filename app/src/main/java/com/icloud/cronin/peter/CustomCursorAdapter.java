package com.icloud.cronin.peter;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
	
	public CustomCursorAdapter(Context context, Cursor c){
		super(context, c);
	}

	

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View reView = inflater.inflate(R.layout.single_row_item, parent, false);
		
		return reView;
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView textViewCourse = (TextView)view.findViewById(R.id.tv_course);
		textViewCourse.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)))),TextView.BufferType.SPANNABLE);
		
		TextView textViewCourseNumber = (TextView)view.findViewById(R.id.tv_course_number);
		textViewCourseNumber.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
		
	}
}
