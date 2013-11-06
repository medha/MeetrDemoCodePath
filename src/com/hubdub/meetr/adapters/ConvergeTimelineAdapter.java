package com.hubdub.meetr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConvergeTimelineAdapter extends ArrayAdapter<String> {
	Context mContext;

	public ConvergeTimelineAdapter(Context context, ArrayList<String> arrayList) {
		super(context, 0, arrayList);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.converge_timeline_item, parent,false);
			
		}

		 String dummy = getItem(position);

			TextView tvChangeDate = (TextView) view.findViewById(com.hubdub.meetr.R.id.tvChangeDate);
			TextView tvConverge = (TextView) view.findViewById(com.hubdub.meetr.R.id.tvConverge);

			tvChangeDate.setText("4:00pm");
			tvConverge.setText(dummy);
			
		return view;
	}
	

}
