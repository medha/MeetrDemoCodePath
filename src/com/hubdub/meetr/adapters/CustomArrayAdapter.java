package com.hubdub.meetr.adapters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hubdub.meetr.models.Events;

public class CustomArrayAdapter extends com.haarman.listviewanimations.ArrayAdapter<Events> {
	Context mContext;

	public CustomArrayAdapter(Context context, ArrayList<Events> items) {
		super(items);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.event_list, parent,false);
		}

		Events event = getItem(position);

		// Do additional configuration before returning the View.
		TextView tvEventName = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventName);
		TextView tvEventDate = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventDate);
		TextView tvEventTime = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventTime);
		TextView tvLocation = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.tvLocation);
		TextView tvFriendsLabel = (TextView) view.findViewById(com.hubdub.meetr.R.id.tvFriendsLabel);
		
		tvEventName.setText(event.getString("EventName"));

		// set date and time
		Long newDate = event.getEventDate().getTime();
		String date = DateFormat.format("EEE, MMM dd", newDate).toString();
		String time = DateFormat.format("h:mm a", newDate).toString();
		tvEventDate.setText(date);
		tvEventTime.setText(time);

		// set location
		String location = event.getLocation();
		if(location == null) {
			tvLocation.setText("No location set");
		} else {
			String[] locationSplit = location.split("\n");
			tvLocation.setText(locationSplit[0] + ", " + locationSplit[1]);
		}
		
		
		// set friends invited
		JSONArray guestList = (JSONArray) event.getGuestList();
		String gList = "";
		
		int length = guestList.length();
		
		if (length == 0) {
			gList = "No friends invited yet. Let's change that.";
		} else if (length == 1) {
			try {
				gList = gList + ((JSONObject) guestList.get(0)).getString("name");
			} catch (JSONException e) {
				Log.d("ERROR", e.toString());
				gList = "";
			}
		} else {
			
			for (int i = 0; i < guestList.length(); i++) {
				try {
					gList = gList
							+ ((JSONObject) guestList.get(i)).getString("name")
							+ ", ";
				} catch (JSONException e) {
					Log.d("ERROR", e.toString());
					gList = "";
				}
			}
		}
		tvFriendsLabel.setText(gList);

		return view;
	}
	

}
