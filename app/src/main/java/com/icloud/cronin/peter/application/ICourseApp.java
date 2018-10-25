package com.icloud.cronin.peter.application;

import android.app.Application;

import com.icloud.cronin.peter.data.database.ICourseDatabase;

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
