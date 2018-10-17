package com.icloud.cronin.peter.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.model.RaceCourse;

public class ListAdapter extends BaseAdapter{
	
	private List<RaceCourse> raceList;
	private LayoutInflater inflater;
	
	public ListAdapter(Context context,List<RaceCourse> raceList) {
		inflater = ((Activity) context).getLayoutInflater();
		this.raceList = raceList;
	}
	

	@Override
	public int getCount() {
		return raceList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return raceList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.single_row_item,parent, false);
         TextView course=(TextView) view.findViewById(R.id.tv_course);
         TextView course_number=(TextView) view.findViewById(R.id.tv_course_number);
         course.setText(Html.fromHtml(raceList.get(position).getCourse()),TextView.BufferType.SPANNABLE);
         course_number.setText(raceList.get(position).getCourseNumber());
         return view;
	}

}
