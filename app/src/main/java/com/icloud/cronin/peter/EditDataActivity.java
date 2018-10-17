package com.icloud.cronin.peter;

import com.icloud.cronin.peter.adapter.SpinnerAdapter;
import com.icloud.cronin.peter.datasource.RaceCourseDBDataSource;
import com.icloud.cronin.peter.util.ValidationUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditDataActivity extends Activity implements OnClickListener {

	Button ViewData, GetInfo, EditEntry, DeleteEntry, Exit;
	EditText etCourse, etCourseNumber, etCourseDetails;
	Intent intent;
	private String course,course_number,finaltext;
	Spinner colordropdown;
	String[] colors = {"Black","Red","Green"};
	private DBHelper databaseHelper;
	private RaceCourseDBDataSource raceDBDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_data);
		intent = getIntent();

		if(null != intent ){
			course = intent.getStringExtra("course");
			course_number = intent.getStringExtra("course_number");
			finaltext = course;
		}

		colordropdown = (Spinner)findViewById(R.id.colordropdown);
		SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item_layout,colors);
		colordropdown.setAdapter(adapter);
		databaseHelper = DBHelper.getInstance(this);

		etCourse = (EditText)findViewById(R.id.et_course);
		etCourseNumber = (EditText)findViewById(R.id.et_course_number);
		etCourseDetails = (EditText)findViewById(R.id.et_course_details);
		etCourseNumber.setText(course_number);
		etCourseDetails.setText(Html.fromHtml(course),TextView.BufferType.SPANNABLE);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()){

		case R.id.deleteBt:
			if(databaseHelper.deleteEntry(course_number)!=0){
				Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show();
				this.finish();
			}
			break;
		case R.id.updateBt:
			if(databaseHelper.updateEntry(course_number,finaltext)!=0){
				Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();
				this.finish();
			}
			break;
		case R.id.addCourse:
			if(ValidationUtil.checkValidField(this, etCourse)){
				String color = (String)colordropdown.getSelectedItem();
				String newtext = etCourse.getText().toString();
				finaltext = finaltext + "&nbsp;<font color='"+color.toLowerCase()+"'>"+newtext+"</font>" ;
				etCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
			}
			break;
		case R.id.removeCourse:
			if(!finaltext.trim().equals("")){
				finaltext = finaltext.substring(0,finaltext.lastIndexOf("&nbsp;"));
				etCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
			}
			break;
		}
	}
}

