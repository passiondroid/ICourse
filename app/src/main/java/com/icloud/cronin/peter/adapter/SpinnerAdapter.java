package com.icloud.cronin.peter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icloud.cronin.peter.R;

public class SpinnerAdapter extends ArrayAdapter<String>{
	private String[] items;
	private Context mContext;
	private LayoutInflater inflater;
	
	public SpinnerAdapter(Context mContext,int resourceId, String[] items){
		super(mContext, resourceId, items);
		this.items = items;
		this.mContext = mContext;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
	
	public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
	
	// This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_item_layout, parent, false);
        TextView tv        = (TextView)row.findViewById(R.id.tv);
        tv.setText(items[position]);
        return row;
      }

}
