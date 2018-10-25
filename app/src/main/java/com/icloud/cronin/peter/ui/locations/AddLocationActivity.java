package com.icloud.cronin.peter.ui.locations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceLocation;
import com.icloud.cronin.peter.ui.editlocation.EditLocationActivity;
import com.icloud.cronin.peter.util.Constants;

/**
 * Created by Arif Khan on 22/10/18.
 */
public class AddLocationActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText latitudeET;
    private EditText longitudeET;
    private EditText markerET;
    private Button updateBtn;
    private ICourseDatabase database = ICourseApp.iCourseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_location_edit);
        setTitle("Add Location");
        setUpViews();
        setListener();
    }

    private void setUpViews() {
        nameET = findViewById(R.id.nameET);
        latitudeET = findViewById(R.id.latitudeET);
        longitudeET = findViewById(R.id.longitudeET);
        markerET = findViewById(R.id.markerET);
        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setText("Add");
    }

    private void setListener(){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RaceLocation raceLocation = new RaceLocation(
                        nameET.getText().toString(),
                        latitudeET.getText().toString(),
                        longitudeET.getText().toString(),
                        markerET.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            long i = database.locationDao().insert(raceLocation);
                            if( i != -1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddLocationActivity.this, "Location Added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddLocationActivity.this, "Failed to add Location", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

}
