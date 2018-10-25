package com.icloud.cronin.peter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icloud.cronin.peter.R;
import com.icloud.cronin.peter.data.model.RaceLocation;

import java.util.List;

public class LocationListAdapter extends BaseAdapter {

	private List<RaceLocation> raceLocation;

	public LocationListAdapter(List<RaceLocation> raceLocation){
	    this.raceLocation = raceLocation;
	}

	@Override
	public int getCount() {
		return raceLocation.size();
	}

	@Override
	public RaceLocation getItem(int position) {
		return raceLocation.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        CourseItemViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.layout_location_item, null);
            holder = new CourseItemViewHolder();
            holder.nameTV = convertView.findViewById(R.id.nameTV);
            holder.latitudeTV = convertView.findViewById(R.id.latitudeTV);
            holder.longitudeTV = convertView.findViewById(R.id.longitudeTV);
            holder.markerTV = convertView.findViewById(R.id.markerTV);
            convertView.setTag(holder);
        }
        else {
            holder = (CourseItemViewHolder) convertView.getTag();
        }

        RaceLocation raceLocation = getItem(position);
        holder.nameTV.setText("Name: "+raceLocation.getLocationName());
        holder.latitudeTV.setText("Latitude: "+raceLocation.getLatitude());
        holder.longitudeTV.setText("Longitude: "+raceLocation.getLongitude());
        holder.markerTV.setText("Marker: "+raceLocation.getMarkerCharacter());

        return convertView;
	}

    static class CourseItemViewHolder {
        private TextView nameTV;
        private TextView latitudeTV;
        private TextView longitudeTV;
        private TextView markerTV;
    }
}
