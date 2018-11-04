package com.icloud.cronin.peter.ui.addcourse;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.adapter.SpinnerAdapter;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceCourse;
import com.icloud.cronin.peter.util.Constants;
import com.icloud.cronin.peter.util.ValidationUtil;
import java.util.Date;

public class EnterDataActivity extends Activity   {

	Spinner courseDropdown;
	EditText editTextCourseNumber;
	EditText editTextCourseDetails;
	Spinner colordropdown;
	String[] colors = {"Black","Red","Green", "Yellow"};
	String finaltext = "";
	private ICourseDatabase database = ICourseApp.iCourseDatabase;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_data);
		courseDropdown = findViewById(R.id.courseDropdown);
		editTextCourseNumber = findViewById(R.id.et_course_number);
		editTextCourseDetails = findViewById(R.id.et_course_details);
		colordropdown = findViewById(R.id.colordropdown);
		SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item_layout,colors);
		colordropdown.setAdapter(adapter);
		setCourseDropdown();
	}

	private void setCourseDropdown() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String[] markers = database.locationDao().getAllMarkers();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						SpinnerAdapter courseAdapter = new SpinnerAdapter(EnterDataActivity.this, R.layout.spinner_item_layout,markers);
						courseDropdown.setAdapter(courseAdapter);
					}
				});
			}
		}).start();
	}

	public  void onClickAdd(View btnAdd){
		if(ValidationUtil.checkValidField(this, editTextCourseNumber) && ValidationUtil.checkValidField(this, editTextCourseDetails)){
			String courseNumber = editTextCourseNumber.getText().toString();
			RaceCourse raceCourse = new RaceCourse();
			raceCourse.setCourse(finaltext);
			raceCourse.setCourseNumber(courseNumber);
			raceCourse.setDateAdded(new Date());
            addCourseToDatabase(raceCourse);
		}
	}

	public void onClick(View view){
		if(view.getId()==R.id.addCourse){
//			if(ValidationUtil.checkValidField(this, editTextCourse)){
				String color = (String)colordropdown.getSelectedItem();
				String newtext = (String) courseDropdown.getSelectedItem();
				if(color.equalsIgnoreCase("red")){
					color = Constants.red;
				}else if(color.equalsIgnoreCase("black")){
					color = Constants.black;
				}else if(color.equalsIgnoreCase("green")){
					color = Constants.green;
				}else if(color.equalsIgnoreCase("yellow")){
					color = Constants.yellow;
				}
				finaltext = finaltext + "&nbsp;<font color='"+color+"'>"+newtext+"</font>" ;
				editTextCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
//				editTextCourse.setText("");
//			}
		}else if(view.getId()==R.id.removeCourse){
			if(!finaltext.trim().equals("")){
				finaltext = finaltext.substring(0,finaltext.lastIndexOf("&nbsp;"));
				editTextCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
			}
		}
	}

	private void addCourseToDatabase(final RaceCourse raceCourse) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final long value = database.raceCourseDao().addRaceCourse(raceCourse);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
                        if(value != -1){
                            finish();
                        }else{
                            Toast.makeText(EnterDataActivity.this, "Course not added", Toast.LENGTH_SHORT).show();
                        }
					}
				});
			}
		}).start();
	}
}