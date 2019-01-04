package com.icloud.cronin.peter.application;

import android.app.Application;

import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceCourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class ICourseApp extends Application{

	public static ICourseDatabase iCourseDatabase;
	
	@Override
	public void onCreate() {
		super.onCreate();
		iCourseDatabase = ICourseDatabase.getInstance(this);
		new Thread(new Runnable() {
            @Override
            public void run() {
                iCourseDatabase.raceCourseDao().getAllCourses();
            }
        }).start();
	}
}
