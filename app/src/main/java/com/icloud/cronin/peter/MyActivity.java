package com.icloud.cronin.peter;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.icloud.cronin.peter.adapter.ListAdapter;
import com.icloud.cronin.peter.datasource.RaceCourseDBDataSource;
import com.icloud.cronin.peter.loader.RaceCourseDataLoader;
import com.icloud.cronin.peter.model.RaceCourse;
import com.icloud.cronin.peter.util.ViewUtils;

public class MyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<RaceCourse>>  {

	private CustomCursorAdapter customAdapter;
	private DBHelper databaseHelper;
	private ListView listView;
	private SQLiteDatabase mDatabase;
	private RaceCourseDBDataSource mDataSource;
	private DBHelper mDbHelper;
	private Dialog dialog;

	private static final String TAG = MyActivity.class.getSimpleName();



	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listv);

		/* Button bEditData = (Button)findViewById(R.id.bEditdata);
		bEditData.setOnClickListener(new OnClickListener(){

			public void onClick(View v){
				Intent y = new Intent("com.icloud.cronin.peter.SQLEdit");
				startActivity(y);
			}
		});*/

		mDbHelper = DBHelper.getInstance(this);
		mDatabase = mDbHelper.getWritableDatabase();
		mDataSource = new RaceCourseDBDataSource(mDatabase);

		/*Button button2 = (Button) findViewById(R.id.bgetcourse);
		button2.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				MyActivity.this.finish();
			}
		});*/

		databaseHelper = DBHelper.getInstance(this);

		listView = (ListView) findViewById(R.id.list);
		//final ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Intent intent = new Intent(MyActivity.this, EditDataActivity.class);
				RaceCourse raceCourse = (RaceCourse)listView.getAdapter().getItem(position);
				intent.putExtra("course_number", raceCourse.getCourseNumber());
				intent.putExtra("course", raceCourse.getCourse());
				startActivity(intent);
			}
		});

		new Handler().post(new Runnable(){
			@Override
			public void run(){
				customAdapter = new CustomCursorAdapter(MyActivity.this, databaseHelper.getAllData());
				listView.setAdapter(customAdapter);
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_sync:
	        	dialog = ViewUtils.showSyncDialog(MyActivity.this);
	        	break;
	        	//Intent i = new Intent("com.icloud.cronin.peter.MyActivity");
				//startActivity(i);
	            //return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
        return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSupportLoaderManager().initLoader(1, null, this);
	}

	public void onClickEnterData(View btnAdd) {
		startActivity(new Intent(this, EnterDataActivity.class));
	}

	/*@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data){
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK){
				databaseHelper.insertData(data.getExtras() .getString("tag_course"), data.getExtras().getString("tag_course_number"),data.getExtras() .getString("tag_color"));

				customAdapter.changeCursor(databaseHelper.getAllData());
			}

		}*/

	@Override
	public Loader<List<RaceCourse>> onCreateLoader(int arg0, Bundle arg1) {
		RaceCourseDataLoader  loader = new RaceCourseDataLoader(getApplicationContext(), mDataSource, null, null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<RaceCourse>> arg0,List<RaceCourse> raceList) {
		if(null != raceList && raceList.size()>0){
			ListAdapter adapter=new ListAdapter(this,raceList);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<RaceCourse>> arg0) {
		// TODO Auto-generated method stub

	}

}





