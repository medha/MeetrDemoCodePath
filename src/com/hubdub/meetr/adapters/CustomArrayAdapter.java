package com.hubdub.meetr.adapters;


import java.util.ArrayList;

import com.hubdub.meetr.models.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<Event>{

	public CustomArrayAdapter(Context context, ArrayList<Event> events) {
		super(context, com.hubdub.meetr.R.layout.event_list, events);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		Event event = getItem(position);
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(com.hubdub.meetr.R.layout.event_list, null);
		}
		TextView tvEventName = (TextView) view.findViewById(com.hubdub.meetr.R.id.eventName);
		TextView tvEventDate = (TextView) view.findViewById(com.hubdub.meetr.R.id.eventDate);
		TextView tvEventTime= (TextView) view.findViewById(com.hubdub.meetr.R.id.eventTime);
		TextView tvEventMonth= (TextView) view.findViewById(com.hubdub.meetr.R.id.eventMonth);
		tvEventName.setText(event.getEventName());
		tvEventDate.setText(event.getEventDate());
		tvEventTime.setText(event.getEventTime());
		tvEventMonth.setText("Nov");
		return view;
	}
	
}
