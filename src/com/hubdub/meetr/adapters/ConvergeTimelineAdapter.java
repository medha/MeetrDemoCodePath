package com.hubdub.meetr.adapters;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.models.EventActivity;
import com.parse.ParseObject;

public class ConvergeTimelineAdapter extends ArrayAdapter<EventActivity> {
	Context mContext;

	public ConvergeTimelineAdapter(Context context, ArrayList<EventActivity> arrayList) {
		super(context, 0, arrayList);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.converge_timeline_item, parent,false);
			
		}
		
		EventActivity activity = getItem(position);
        ParseObject eventObj = activity.getParseObject("activityFrom");
        ParseObject obj = activity.getParseObject("suggestionPtr");
        
        TextView tvPostBy = (TextView) view.findViewById(R.id.tvPostBy);
        
        try {
			tvPostBy.setText((eventObj.getJSONObject("profile").getString("name")) + " suggests:");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView tvPost = (TextView) view.findViewById(com.hubdub.meetr.R.id.tvPost);
		Date mEventDate = obj.getDate("EventDate");
		Date mEventTime = obj.getDate("EventTime");
		String date = DateFormat.format("MMM dd", mEventDate).toString();
		String time = DateFormat.format("h:mm a", mEventTime).toString();
		String location = obj.getString("Location");
		String suggestion_line1 = date + ", " + time + " at " + location;
		
		tvPost.setText(suggestion_line1);
		
		return view;
	}
	

}
