package com.icloud.cronin.peter.application;

import android.app.Application;

import com.icloud.cronin.peter.Constants;
import com.parse.Parse;

public class HYCRacingApp extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, Constants.APPLICATION_ID, Constants.CLIENT_KEY);
	}

}
