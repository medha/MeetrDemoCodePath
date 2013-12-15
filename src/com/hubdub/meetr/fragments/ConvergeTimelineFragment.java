package com.hubdub.meetr.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.ConvergeTimelineAdapter;
import com.hubdub.meetr.models.EventActivity;
import com.hubdub.meetr.models.Suggestions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ConvergeTimelineFragment extends Fragment{
	ListView lvConvergeItems;
	private Object eventId;
	private List<EventActivity> eventActivity;
	private ConvergeTimelineAdapter adapter;
	private ListView listView;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_converge_timeline, container, false);
		ParseObject.registerSubclass(EventActivity.class);
		ParseObject.registerSubclass(Suggestions.class);
		
		Parse.initialize(getActivity(), "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");
		
		return view;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		listView = (ListView) getView().findViewById(R.id.lvConvergeItems);
		
		Intent i = getActivity().getIntent();
		Bundle extras = i.getExtras();

		eventId = extras.getString("EventId");
		
		loadData();
	}
	
	private ParseQuery<EventActivity> fetchEventActivityItems() {
		/*
		 * Doing a join query here. Requesting all rows where the event is
		 * created by this user and also where this user is an invited guest.
		 */
		ParseQuery<EventActivity> query = new ParseQuery<EventActivity>("Activity");
		query.whereEqualTo("eventObj", eventId);
		query.whereEqualTo("activityType", "suggestion");
		query.include("activityFrom");
		query.include("suggestionPtr");
		query.orderByDescending("createdAt");
		return query;
	}

	/*
	 * Functon to Load data into the listview
	 */
	public void loadData() {
		
		ParseQuery<EventActivity> query = fetchEventActivityItems();
			query.findInBackground(new FindCallback<EventActivity>(){
				@Override
				public void done(List<EventActivity> object, ParseException e) {
					if (e == null) {
						eventActivity = object;
						adapter = new ConvergeTimelineAdapter(getActivity(), new ArrayList<EventActivity>());
						listView.setAdapter(adapter);
						adapter.clear();
						adapter.addAll(eventActivity);
						adapter.notifyDataSetChanged();
					} else {
						Log.d("ERROR", e.getMessage());
					}
				}
			});
	}

}
