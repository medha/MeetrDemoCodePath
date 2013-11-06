package com.hubdub.meetr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.models.Activities;


public class EventTimelineAdapter extends ArrayAdapter<Activities> {
	private Context mContext;

	public EventTimelineAdapter(Context context, ArrayList<Activities> activities) {
		super(context, 0, activities);
		
		this.mContext = context;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.timeline_item, parent,false);
		}

		Activities activity = getItem(position);
		
		TextView tvPost = (TextView) view.findViewById(R.id.tvPost);
		ImageView ivImageTimeline = (ImageView) view.findViewById(R.id.ivImageTimeline);
		
		tvPost.setText(activity.getDummyActivity());
		
		
		return view;
	}

}
