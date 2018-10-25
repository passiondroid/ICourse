package com.icloud.cronin.peter.ui.getcourse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.model.RaceCourse;
import com.icloud.cronin.peter.ui.courselist.CoursesActivity;
import com.icloud.cronin.peter.ui.locations.SailingLocationActivity;
import com.icloud.cronin.peter.ui.maps.MapsActivity;
import com.icloud.cronin.peter.util.Constants;
import com.icloud.cronin.peter.util.ValidationUtil;

public class CourseInformationActivity extends AppCompatActivity implements OnClickListener {

	private static final String TAG = "SplashActivity";
	private static final int ERROR__DIALOG_REQUEST = 9001;

	Button sqlGetCourse;
	EditText courseDetails;
	EditText sqlRow;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_information);
		sqlGetCourse = findViewById(R.id.bgetcourse);
		courseDetails = findViewById(R.id.et_course_details);
		sqlRow = findViewById(R.id.etSQLrowInfo);
		sqlGetCourse.setOnClickListener(this);

		if (isServicesOK()){
			init();
		}
	}

	private void init(){
		Button btnMap = findViewById(R.id.btnMap);
		btnMap.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
			    if(!TextUtils.isEmpty(courseDetails.getText().toString())) {
                    String course = courseDetails.getText().toString().trim();
                    Intent intent = new Intent(CourseInformationActivity.this, MapsActivity.class);
                    intent.putExtra(Constants.COURSE,course);
                    startActivity(intent);
                }else {
                    Toast.makeText(CourseInformationActivity.this, "No Course to show on map", Toast.LENGTH_SHORT).show();
                }
			}
		});

        sqlRow.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    getRaceCourseAndUpdateUI();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
	}

	public boolean isServicesOK() {
		Log.d(TAG, "isServicesOK: checking google services version");

		int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CourseInformationActivity.this);

		if (available == ConnectionResult.SUCCESS) {
			Log.d(TAG, "isServicesOK: Google play services is working");
			return true;
		} else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
			Log.d(TAG, "isServicesOK: an error occured we can fix it");
			Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CourseInformationActivity.this, available, ERROR__DIALOG_REQUEST);
			dialog.show();
		} else {
			Toast.makeText(this, "You cannot make map request", Toast.LENGTH_SHORT).show();
		}
		return false;
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
	        	Intent i = new Intent(this, CoursesActivity.class);
				startActivity(i);
	            return true;
			case R.id.action_locations:
				Intent intent = new Intent(this, SailingLocationActivity.class);
				startActivity(intent);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onClick(View arg0){
		if(arg0.getId() == R.id.bgetcourse){
            getRaceCourseAndUpdateUI();
		}
	}

    private void getRaceCourseAndUpdateUI() {
        if(ValidationUtil.checkValidField(this, sqlRow)){
            final String s = sqlRow.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final RaceCourse raceCourse = ICourseApp.iCourseDatabase.raceCourseDao().findCourseByNumber(s);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI(raceCourse);
                        }
                    });
                }
            }).start();
        }
    }

    private void updateUI(RaceCourse raceCourse) {
	    if(raceCourse != null) {
            courseDetails.setText(Html.fromHtml(raceCourse.getCourse()), TextView.BufferType.SPANNABLE);
        }else {
            Toast.makeText(CourseInformationActivity.this, "No Course Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
