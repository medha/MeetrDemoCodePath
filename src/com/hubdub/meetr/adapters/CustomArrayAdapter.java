package com.hubdub.meetr.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hubdub.meetr.models.Events;
import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class CustomArrayAdapter extends ParseQueryAdapter<Events>{

	public CustomArrayAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Events>() {
			public ParseQuery<Events> create() {
				String fbId = "";
				// Here we can configure a ParseQuery to our heart's desire.
				try {
					fbId = ParseUser.getCurrentUser().getJSONObject("profile").getString("facebookId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* Doing a join query here. Requesting all rows where the event is 
				 * created by this user and also where this user is an invited guest.
				 */
				ParseQuery<Events> queryObjId = new ParseQuery<Events>("Events");
				queryObjId.whereEqualTo("User", ParseUser.getCurrentUser());
				ParseQuery<Events> queryFbId = new ParseQuery<Events>("Events");
				queryFbId.whereEqualTo("FbGuestList", fbId);
				List<ParseQuery<Events>> queries = new ArrayList<ParseQuery<Events>>();
			    queries.add(queryObjId);
			    queries.add(queryFbId);
			    ParseQuery<Events> query = new ParseQuery<Events>("Events");
				query = ParseQuery.or(queries);
				query.orderByDescending("updatedAt");
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
