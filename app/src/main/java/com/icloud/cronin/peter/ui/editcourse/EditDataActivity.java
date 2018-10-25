package com.icloud.cronin.peter.ui.editcourse;

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
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.adapter.SpinnerAdapter;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceCourse;
import com.icloud.cronin.peter.util.ValidationUtil;
import java.util.Date;

public class EditDataActivity extends Activity implements OnClickListener {

	Button ViewData, GetInfo, EditEntry, DeleteEntry, Exit;
	private Spinner courseDropdown;
	private EditText etCourseNumber, etCourseDetails;
	private Intent intent;
	private String course;
	private String course_number;
	private String finaltext;
	Spinner colordropdown;
	ICourseDatabase database = ICourseApp.iCourseDatabase;
	String[] colors = {"Black","Red","Green"};

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

		colordropdown = findViewById(R.id.colordropdown);
		SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item_layout,colors);
		colordropdown.setAdapter(adapter);
		setCourseDropdown();

		courseDropdown = findViewById(R.id.courseDropdown);
		etCourseNumber = findViewById(R.id.et_course_number);
		etCourseDetails = findViewById(R.id.et_course_details);
		etCourseNumber.setText(course_number);
		etCourseDetails.setText(Html.fromHtml(course),TextView.BufferType.SPANNABLE);
	}

	private void setCourseDropdown() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String[] markers = database.locationDao().getAllMarkers();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						SpinnerAdapter courseAdapter = new SpinnerAdapter(EditDataActivity.this, R.layout.spinner_item_layout,markers);
						courseDropdown.setAdapter(courseAdapter);
					}
				});
			}
		}).start();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()){

		case R.id.deleteBt:
            deleteCourseFromDatabase(course_number);
			break;

		case R.id.updateBt:
            RaceCourse raceCourse = new RaceCourse();
            raceCourse.setCourseNumber(course_number);
            raceCourse.setCourse(finaltext);
            raceCourse.setDateAdded(new Date());
            updateCourseInDatabase(raceCourse);
			break;

		case R.id.addCourse:
//			if(ValidationUtil.checkValidField(this, etCourse)){
				String color = (String)colordropdown.getSelectedItem();
				String newtext = (String)courseDropdown.getSelectedItem();
				finaltext = finaltext + "&nbsp;<font color='"+color.toLowerCase()+"'>"+newtext+"</font>" ;
				etCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
//			}
			break;
		case R.id.removeCourse:
			if(!finaltext.trim().equals("")){
				finaltext = finaltext.substring(0,finaltext.lastIndexOf("&nbsp;"));
				etCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
			}
			break;
		}
	}

    private void deleteCourseFromDatabase(final String courseNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long value = database.raceCourseDao().deleteCourseByNumber(courseNumber);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(value != 0){
                            Toast.makeText(EditDataActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EditDataActivity.this, "Course not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void updateCourseInDatabase(final RaceCourse raceCourse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long value = database.raceCourseDao().updateCourse(raceCourse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(value != 0){
                            Toast.makeText(EditDataActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EditDataActivity.this, "Course not updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}

