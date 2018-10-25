package com.icloud.cronin.peter.ui.courselist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.adapter.CourseListAdapter;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceCourse;
import com.icloud.cronin.peter.ui.addcourse.EnterDataActivity;
import com.icloud.cronin.peter.ui.editcourse.EditDataActivity;
import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends AppCompatActivity  {

    private CourseListAdapter courseListAdapter;
    private ListView listView;
    private ICourseDatabase database = ICourseApp.iCourseDatabase;
    private List<RaceCourse> raceCourses = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listv);
        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(CoursesActivity.this, EditDataActivity.class);
                RaceCourse raceCourse = (RaceCourse)listView.getAdapter().getItem(position);
                intent.putExtra("course_number", raceCourse.getCourseNumber());
                intent.putExtra("course", raceCourse.getCourse());
                startActivity(intent);
            }
        });

        courseListAdapter = new CourseListAdapter(raceCourses);
        listView.setAdapter(courseListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRaceCoursesAndUpdateUI();
    }

    public void onClickEnterData(View btnAdd) {
        startActivity(new Intent(this, EnterDataActivity.class));
    }

    private void getRaceCoursesAndUpdateUI() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<RaceCourse> raceCoursesDb = database.raceCourseDao().getAllCourses();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            raceCourses.clear();
                            raceCourses.addAll(raceCoursesDb);
                            courseListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
    }
}





