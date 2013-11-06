package com.hubdub.meetr.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hubdub.meetr.R;
import com.hubdub.meetr.activities.TimelineActivity;
import com.hubdub.meetr.adapters.EventTimeLnAdapter;
import com.hubdub.meetr.models.EventActivity;
import com.hubdub.meetr.models.Posts;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class EventTimelineFragment extends Fragment {
	private EventTimeLnAdapter adapter;
	private ListView listView;
	private String eventId;
	List<EventActivity> eventActivity = new ArrayList<EventActivity>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_timeline, container, false);
		
		/*
		 * Custom adapter serves up the Event feed for the current logged in
		 * user
		 */
		ParseObject.registerSubclass(EventActivity.class);
		ParseObject.registerSubclass(Posts.class);
		
		Parse.initialize(getActivity(), "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");

		Intent i = getActivity().getIntent();
		eventId = i.getStringExtra("EventId");
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		loadData();
		
		listView = (ListView) getView().findViewById(R.id.lvItems);
		ImageButton submit_button = (ImageButton) getView().findViewById(R.id.submit_button);
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCommentPost(v);
			}
		});
		
		ParseQuery<EventActivity> query = fetchEventActivityItems();
			query.findInBackground(new FindCallback<EventActivity>(){
				@Override
				public void done(List<EventActivity> object, ParseException e) {
					eventActivity = object;
					adapter = new EventTimeLnAdapter(getActivity(), new ArrayList<EventActivity>());
					listView.setAdapter(adapter);
					adapter.addAll(eventActivity);
				}
			});
	}
	
	
	private ParseQuery<EventActivity> fetchEventActivityItems() {
		String fbId = "";
		// Here we can configure a ParseQuery to our heart's desire.
		try {
			fbId = ParseUser.getCurrentUser().getJSONObject("profile")
					.getString("facebookId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/*
		 * Doing a join query here. Requesting all rows where the event is
		 * created by this user and also where this user is an invited guest.
		 */
		ParseQuery<EventActivity> query = new ParseQuery<EventActivity>("Activity");
		query.whereEqualTo("eventObj", eventId);
		query.include("activityFrom");
		query.include("postPtr");
		query.orderByDescending("createdAt");
		return query;
	}

	/*
	 * Functon to Load data into the listview
	 */
	public void loadData() {

		listView = (ListView) getView().findViewById(R.id.lvItems);
		
		ParseQuery<EventActivity> query = fetchEventActivityItems();
			query.findInBackground(new FindCallback<EventActivity>(){
				@Override
				public void done(List<EventActivity> object, ParseException e) {
					eventActivity = object;
					adapter = new EventTimeLnAdapter(getActivity(), new ArrayList<EventActivity>());
					listView.setAdapter(adapter);
					adapter.addAll(eventActivity);
				}
			});
	}
	
	
	public void onCommentPost(View view){
		
		final Posts post = new Posts();
		//Save in the post table
		final ParseUser user = ParseUser.getCurrentUser();
		post.put("userPtr", user);
		final JSONObject eventPtr = new JSONObject();
		
		try {
			eventPtr.put("__type", "Pointer");
			eventPtr.put("className", "Events");
			eventPtr.put("objectId", eventId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post.put("eventPtr", eventPtr);
		EditText postText = (EditText) getView().findViewById(R.id.etPost);
		post.put("post", postText.getText().toString());
		post.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				//create entry in the activity table
		 		EventActivity eActivity = new EventActivity();
		 		String postId = post.getObjectId();
		 		JSONObject postPtr = new JSONObject();
				try {
					postPtr.put("__type", "Pointer");
					postPtr.put("className", "Posts");
					postPtr.put("objectId", postId);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				eActivity.put("postPtr", postPtr);
				eActivity.put("activityFrom", user);
				eActivity.put("eventPtr", eventPtr);
				eActivity.put("eventObj", eventId);				
			
			eActivity.saveInBackground(new SaveCallback(){
				@Override
				public void done(ParseException e) {
					ParseQuery<EventActivity> query = fetchEventActivityItems();
					query.findInBackground(new FindCallback<EventActivity>(){

						@Override
						public void done(List<EventActivity> objects,
								ParseException e) {
							eventActivity = objects;
							adapter.clear();
							adapter.addAll(eventActivity);
							listView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
						
					});

				}
				});
			}
			
		});
	
		EventActivity loadingObject = new EventActivity();
		loadingObject.setObjectId(null);
		eventActivity.add(0, loadingObject);
		adapter.notifyDataSetChanged();
	}

}
