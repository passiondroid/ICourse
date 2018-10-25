package com.icloud.cronin.peter.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.data.model.RaceCourse;

import java.util.List;

public class CourseListAdapter extends BaseAdapter {

	private List<RaceCourse> raceCourseList;
	
	public CourseListAdapter(List<RaceCourse> raceCourseList){
	    this.raceCourseList = raceCourseList;
	}

	@Override
	public int getCount() {
		return raceCourseList.size();
	}

	@Override
	public RaceCourse getItem(int position) {
		return raceCourseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        CourseItemViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.single_row_item, null);
            holder = new CourseItemViewHolder();
            holder.tv_course = convertView.findViewById(R.id.tv_course);
            holder.tv_course_number = convertView.findViewById(R.id.tv_course_number);
            convertView.setTag(holder);
        }
        else {
            holder = (CourseItemViewHolder) convertView.getTag();
        }

        RaceCourse raceCourse = getItem(position);
        holder.tv_course.setText(Html.fromHtml(raceCourse.getCourse()),TextView.BufferType.SPANNABLE);
        holder.tv_course_number.setText(raceCourse.getCourseNumber());

        return convertView;
	}

    static class CourseItemViewHolder {
        private TextView tv_course;
        private TextView tv_course_number;
    }
}
