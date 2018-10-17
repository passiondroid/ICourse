package com.icloud.cronin.peter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.icloud.cronin.peter.util.ValidationUtil;

public class Course_Information extends AppCompatActivity implements OnClickListener {

	Button sqlGetCourse, sqlEditData;
	EditText courseDetails, sqlRow;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_information);
		sqlGetCourse = (Button)findViewById(R.id.bgetcourse);
		//sqlEditData = (Button)findViewById(R.id.editData);
		courseDetails = (EditText)findViewById(R.id.et_course_details);
		sqlRow = (EditText)findViewById(R.id.etSQLrowInfo);
		sqlGetCourse.setOnClickListener(this);
		//sqlEditData.setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_edit:
	        	Intent i = new Intent("com.icloud.cronin.peter.MyActivity");
				startActivity(i);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onClick(View arg0){
		switch(arg0.getId()){

		case R.id.bgetcourse:
			if(ValidationUtil.checkValidField(this, sqlRow)){
				String s = sqlRow.getText().toString();
				DBHelper cor = DBHelper.getInstance(this);
				String returnedCourse = cor.getCourse(s);
				courseDetails.setText(Html.fromHtml(returnedCourse),TextView.BufferType.SPANNABLE);
				if(returnedCourse.equals("")){
					Toast.makeText(this, "No Course Found", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}

	}

}
