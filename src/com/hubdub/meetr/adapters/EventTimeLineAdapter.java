package com.hubdub.meetr.adapters;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hubdub.meetr.models.EventActivity;
import com.hubdub.meetr.models.Posts;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

public class EventTimeLineAdapter extends ArrayAdapter<EventActivity> {
	Context mContext;

	public EventTimeLineAdapter(Context context, ArrayList<EventActivity> arrayList) {
		super(context, 0, arrayList);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.timeline_item, parent,false);
			
		}

		final EventActivity event = getItem(position);

		// Do additional configuration before returning the View.
		TextView tvPost = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventPost);
		TextView tvPostBy = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.postBy);
//		TextView tvEventTime = (TextView) view
//				.findViewById(com.hubdub.meetr.R.id.eventTime);
//		TextView tvLocation = (TextView) view
//				.findViewById(com.hubdub.meetr.R.id.tvLocation);
//		
		
		//tvEventName.setText;
		try {
			ParseObject obj = event.getParseObject("postPtr").fetch();
			ParseObject eventObj = event.getParseObject("activityFrom").fetch();
			
			tvPost.setText(obj.getString("post"));
			tvPostBy.setText(eventObj.getJSONObject("profile").getString("name"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return view;
	}
	

}
