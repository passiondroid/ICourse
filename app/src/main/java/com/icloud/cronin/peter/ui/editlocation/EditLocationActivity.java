package com.icloud.cronin.peter.ui.editlocation;

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
import com.icloud.cronin.peter.util.Constants;

/**
 * Created by Arif Khan on 22/10/18.
 */
public class EditLocationActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText latitudeET;
    private EditText longitudeET;
    private EditText markerET;
    private Button updateBtn;
    private ICourseDatabase database = ICourseApp.iCourseDatabase;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_location_edit);
        setTitle("Edit Location");
        setUpViews();
        setListener();
    }

    private void setUpViews() {
        nameET = findViewById(R.id.nameET);
        latitudeET = findViewById(R.id.latitudeET);
        longitudeET = findViewById(R.id.longitudeET);
        markerET = findViewById(R.id.markerET);
        updateBtn = findViewById(R.id.updateBtn);

        id = getIntent().getIntExtra(Constants.LOCATION_ID,-1);
        String name = getIntent().getStringExtra(Constants.LOCATION_NAME);
        String latitude = getIntent().getStringExtra(Constants.LATITUDE);
        String longitude = getIntent().getStringExtra(Constants.LONGITUDE);
        String marker = getIntent().getStringExtra(Constants.MARKER);

        nameET.setText(name);
        latitudeET.setText(latitude);
        longitudeET.setText(longitude);
        markerET.setText(marker);
        nameET.setSelection(name.length());
        latitudeET.setSelection(latitude.length());
        longitudeET.setSelection(longitude.length());
        markerET.setSelection(marker.length());
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
                raceLocation.setId(id);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int i = database.locationDao().updateLocation(raceLocation);
                            if( i == 1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(EditLocationActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(EditLocationActivity.this, "Updation Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
