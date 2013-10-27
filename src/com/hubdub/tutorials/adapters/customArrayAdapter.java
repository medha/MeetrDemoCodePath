package com.hubdub.tutorials.adapters;

import hubdub.tutorials.models.Event;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<Event>{

	public CustomArrayAdapter(Context context, ArrayList<Event> events) {
		super(context, com.hubdub.tutorials.R.layout.event_list, events);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		Event event = getItem(position);
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(com.hubdub.tutorials.R.layout.event_list, null);
		}
		TextView tvEventName = (TextView) view.findViewById(com.hubdub.tutorials.R.id.eventName);
		TextView tvEventDate = (TextView) view.findViewById(com.hubdub.tutorials.R.id.eventDate);
		TextView tvEventTime= (TextView) view.findViewById(com.hubdub.tutorials.R.id.eventTime);
		TextView tvEventMonth= (TextView) view.findViewById(com.hubdub.tutorials.R.id.eventMonth);
		tvEventName.setText(event.getEventName());
		tvEventDate.setText(event.getEventDate());
		tvEventTime.setText(event.getEventTime());
		tvEventMonth.setText("Nov");
		return view;
	}
	
}
