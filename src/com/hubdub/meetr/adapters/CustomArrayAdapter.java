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
import android.widget.ImageView;
import android.widget.TextView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.models.Events;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CustomArrayAdapter extends com.haarman.listviewanimations.ArrayAdapter<Events> {
	Context mContext;
	ImageLoader imageLoader;

	public CustomArrayAdapter(Context context, ArrayList<Events> items) {
		super(items);
		mContext = context;
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.event_list, parent,false);
		}

		Events event = getItem(position);

		// Do additional configuration before returning the View.
		TextView tvEventName = (TextView) view.findViewById(R.id.eventName);
		TextView tvEventDate = (TextView) view.findViewById(R.id.eventDate);
		TextView tvEventMonth = (TextView) view.findViewById(R.id.tvMonth);
		TextView tvEventTime = (TextView) view.findViewById(R.id.eventTime);
		TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
		
		tvEventName.setText(event.getString("EventName"));

		// set date and time
		Long newDate = event.getEventDate().getTime();
		String month = DateFormat.format("MMM", newDate).toString().toUpperCase();
		String date = DateFormat.format("dd", newDate).toString();
		String time = DateFormat.format("h:mm a", newDate).toString();
		tvEventDate.setText(date);
		tvEventMonth.setText(month);
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
		JSONObject data;
		String url = "";
		int width;
		int height;
		
		int length = guestList.length();
		
		if (length == 0) {
			gList = "No friends invited yet. Let's change that.";
			view.findViewById(R.id.ivFriendsProfile1).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.ivFriendsProfile2).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.ivFriendsProfile3).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.ivFriendsProfile4).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.ivFriendsProfile5).setVisibility(View.INVISIBLE);
		} else if (length == 1) {
			try {
				gList = gList + ((JSONObject) guestList.get(0)).getString("name");
				data = ((JSONObject) guestList.get(0)).getJSONObject("picture").getJSONObject("data");
				url = data.getString("url");
				
				imageLoader.displayImage(url, (ImageView) view.findViewById(R.id.ivFriendsProfile1));
				view.findViewById(R.id.ivFriendsProfile1).setVisibility(View.VISIBLE);
				view.findViewById(R.id.ivFriendsProfile2).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.ivFriendsProfile3).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.ivFriendsProfile4).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.ivFriendsProfile5).setVisibility(View.INVISIBLE);
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
					data = ((JSONObject) guestList.get(i)).getJSONObject("picture").getJSONObject("data");
					url = data.getString("url");
					
					if (i == 0) {
						imageLoader.displayImage(url, (ImageView) view.findViewById(R.id.ivFriendsProfile1));
						view.findViewById(R.id.ivFriendsProfile1).setVisibility(View.VISIBLE);
					} else if (i ==1) {
						imageLoader.displayImage(url, (ImageView) view.findViewById(R.id.ivFriendsProfile2));
						view.findViewById(R.id.ivFriendsProfile2).setVisibility(View.VISIBLE);
					} else if (i == 2) {
						imageLoader.displayImage(url, (ImageView) view.findViewById(R.id.ivFriendsProfile3));
						view.findViewById(R.id.ivFriendsProfile3).setVisibility(View.VISIBLE);
					} else if (i == 3) {
						imageLoader.displayImage(url, (ImageView) view.findViewById(R.id.ivFriendsProfile4));
						view.findViewById(R.id.ivFriendsProfile4).setVisibility(View.VISIBLE);
					} else if (i == 4) {
						imageLoader.displayImage(url, (ImageView) view.findViewById(R.id.ivFriendsProfile5));
						view.findViewById(R.id.ivFriendsProfile5).setVisibility(View.VISIBLE);
					}
					
				} catch (JSONException e) {
					Log.d("ERROR", e.toString());
					gList = "";
				}
			}
		}
		return view;
	}
	

}
