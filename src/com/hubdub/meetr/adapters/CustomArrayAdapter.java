package com.hubdub.meetr.adapters;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hubdub.meetr.models.Events;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class CustomArrayAdapter extends ParseQueryAdapter<Events>{

	public CustomArrayAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Events>() {
			public ParseQuery<Events> create() {
				// Here we can configure a ParseQuery to our heart's desire.
				ParseQuery<Events> query = new ParseQuery<Events>("Events");
				query.whereEqualTo("User", ParseUser.getCurrentUser());
				query.orderByDescending("EventDate");
				return query;
			}
		});
	}

	@Override
	public View getItemView(Events event, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(),
					com.hubdub.meetr.R.layout.event_list, null);
		}

		super.getItemView(event, view, parent);

		// Do additional configuration before returning the View.
		TextView tvEventName = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventName);
		TextView tvEventDate = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventDate);
		TextView tvEventTime = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventTime);
		TextView tvEventMonth = (TextView) view
				.findViewById(com.hubdub.meetr.R.id.eventMonth);
		tvEventName.setText(event.getString("EventName"));
		/* TODO fix this so the date and time show properly */
		tvEventDate.setText(String
				.valueOf(event.getDate("EventDate").getDate()));
		Date newDate = event.getDate("EventTime");
		String time = String.valueOf(newDate.getHours()) + ":"
				+ String.valueOf(newDate.getMinutes());
		tvEventTime.setText(time);
		tvEventMonth.setText("Nov");
		return view;
	}

}
