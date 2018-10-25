package com.icloud.cronin.peter.ui.locations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.adapter.LocationListAdapter;
import com.icloud.cronin.peter.application.ICourseApp;
import com.icloud.cronin.peter.data.database.ICourseDatabase;
import com.icloud.cronin.peter.data.model.RaceLocation;
import com.icloud.cronin.peter.ui.editlocation.EditLocationActivity;
import com.icloud.cronin.peter.util.Constants;
import java.util.List;

/**
 * Created by Arif Khan on 22/10/18.
 */
public class SailingLocationActivity extends AppCompatActivity {

    private ICourseDatabase database = ICourseApp.iCourseDatabase;
    private ListView locationListView;
    private LocationListAdapter adapter;
    private List<RaceLocation> locations;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_location_sailing);
        setTitle("Locations");
        locationListView = findViewById(R.id.locationListView);
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocationsAndUpdateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent i = new Intent(this, AddLocationActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLocationsAndUpdateUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locations = database.locationDao().getAllLocations();
                if (locations != null && !locations.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new LocationListAdapter(locations);
                            locationListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    private void setListener() {
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SailingLocationActivity.this, EditLocationActivity.class);
                intent.putExtra(Constants.LOCATION_ID,locations.get(position).getId());
                intent.putExtra(Constants.LOCATION_NAME,locations.get(position).getLocationName());
                intent.putExtra(Constants.LATITUDE,locations.get(position).getLatitude());
                intent.putExtra(Constants.LONGITUDE,locations.get(position).getLongitude());
                intent.putExtra(Constants.MARKER,locations.get(position).getMarkerCharacter());
                startActivity(intent);
            }
        });
    }

}
