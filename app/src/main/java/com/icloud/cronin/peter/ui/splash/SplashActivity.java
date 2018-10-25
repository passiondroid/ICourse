package com.icloud.cronin.peter.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.ui.getcourse.CourseInformationActivity;

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShimmerFrameLayout mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();

        Thread logo = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                    Intent sqlite = new Intent(SplashActivity.this, CourseInformationActivity.class);
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
