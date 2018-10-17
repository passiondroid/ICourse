package com.icloud.cronin.peter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.icloud.cronin.peter.adapter.SpinnerAdapter;
import com.icloud.cronin.peter.util.ValidationUtil;

public class EnterDataActivity extends Activity   {

	EditText editTextCourse;
	EditText editTextCourseNumber,editTextCourseDetails;
	Button Button2; 
	Spinner colordropdown;
	String[] colors = {"Black","Red","Green"};
	String finaltext = "";
	ArrayList<String> textList = new ArrayList<String>();
	private DBHelper databaseHelper;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.enter_data);

		editTextCourse = (EditText) findViewById(R.id.et_course);
		editTextCourseNumber = (EditText) findViewById(R.id.et_course_number);
		editTextCourseDetails = (EditText) findViewById(R.id.et_course_details);
		colordropdown = (Spinner)findViewById(R.id.colordropdown);
		SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item_layout,colors);
		colordropdown.setAdapter(adapter);
		databaseHelper = DBHelper.getInstance(this);

	}
	public  void onClickAdd(View btnAdd){
		if(ValidationUtil.checkValidField(this, editTextCourseNumber) && ValidationUtil.checkValidField(this, editTextCourseDetails)){
			String courseNumber = editTextCourseNumber.getText().toString();
			if(databaseHelper.insertData(finaltext, courseNumber)!=-1){
				finish();
			}else{
				Toast.makeText(this, "Data not added", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void onClick(View view){
		if(view.getId()==R.id.addCourse){
			if(ValidationUtil.checkValidField(this, editTextCourse)){
				String color = (String)colordropdown.getSelectedItem();
				String newtext = editTextCourse.getText().toString();
				if(color.equalsIgnoreCase("red")){
					color = Constants.red;
				}else if(color.equalsIgnoreCase("black")){
					color = Constants.black;
				}else if(color.equalsIgnoreCase("green")){
					color = Constants.green;
				}
				finaltext = finaltext + "&nbsp;<font color='"+color+"'>"+newtext+"</font>" ;
				editTextCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
				editTextCourse.setText("");
			}
		}else if(view.getId()==R.id.removeCourse){
			if(!finaltext.trim().equals("")){
				finaltext = finaltext.substring(0,finaltext.lastIndexOf("&nbsp;"));
				editTextCourseDetails.setText(Html.fromHtml(finaltext),TextView.BufferType.SPANNABLE);
			}
		}
	}
}