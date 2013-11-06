package com.hubdub.meetr.adapters;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubdub.meetr.models.EventActivity;
import com.parse.ParseException;
import com.parse.ParseObject;

public class EventTimeLnAdapter extends ArrayAdapter<EventActivity> {
	Context mContext;

	public EventTimeLnAdapter(Context context, ArrayList<EventActivity> arrayList) {
		super(context, 0, arrayList);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.timeline_item, parent,false);
			
		}

		final EventActivity event = getItem(position);
		String eventType = event.getString("ActivityType");
		
		if (eventType == null) eventType = new String("post");
		if(eventType.equals("post") || (eventType == null)) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.timeline_item, parent,false);
			TextView tvPost = (TextView) view
					.findViewById(com.hubdub.meetr.R.id.eventPost);
			TextView tvPostBy = (TextView) view
					.findViewById(com.hubdub.meetr.R.id.postBy);
			try {
				ParseObject obj = event.getParseObject("postPtr");
				ParseObject eventObj = event.getParseObject("activityFrom");
				
				tvPost.setText(obj.getString("post"));
				tvPostBy.setText(eventObj.getJSONObject("profile").getString("name"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		if(eventType.equals("photo")) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.timeline_photo_item, parent,false);
			ImageView ivPhoto = (ImageView) view
					.findViewById(com.hubdub.meetr.R.id.imageView1);
			TextView tvPostBy = (TextView) view
					.findViewById(com.hubdub.meetr.R.id.postBy);
			ParseObject obj = event.getParseObject("photoPtr");
			ParseObject eventObj = event.getParseObject("activityFrom"); 
		}

		
		return view;
	}
}
