package com.icloud.cronin.peter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      ShimmerFrameLayout mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        DBHelper.getInstance(this).createdatabase();
        
        Thread logo = new Thread(){
        	public void run(){
        		try{
        			//sleep(5000);
        			sleep(6000);
        			Intent sqlite = new Intent("com.icloud.cronin.peter.Course_Information");
        			startActivity(sqlite);
        		}catch(InterruptedException e){
        			e.printStackTrace();
        		}
        	finally{ 
                  finish();
        	}
        }
      };
      
      logo.start();
    }

  }
