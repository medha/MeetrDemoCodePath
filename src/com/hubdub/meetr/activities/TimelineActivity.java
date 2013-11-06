package com.hubdub.meetr.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.EventTimeLineAdapter;
import com.hubdub.meetr.models.EventActivity;
import com.hubdub.meetr.models.Posts;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TimelineActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	private EventTimeLineAdapter adapter;
	private ListView listView;
	private String eventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		/*
		 * Custom adapter serves up the Event feed for the current logged in
		 * user
		 */
		ParseObject.registerSubclass(EventActivity.class);
		ParseObject.registerSubclass(Posts.class);
		
		Parse.initialize(this, "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");

		Intent i = getIntent();
		eventId = i.getStringExtra("EventId");
		loadData();
	}

	/*
	 * Menu/Action bar related stuff goes here
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_compose:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * Functon to Load data into the listview
	 */
	public void loadData() {

		listView = (ListView) findViewById(R.id.lvItems);
		List<EventActivity> eventActivity = new ArrayList<EventActivity>();

		ParseQuery<EventActivity> query = fetchEventActivityItems();

		try {
			eventActivity = query.find();
		} catch (ParseException e) {
			Log.d("ERROR", e.toString());
		}
		adapter = new EventTimeLineAdapter(TimelineActivity.this, new ArrayList<EventActivity>());
		listView.setAdapter(adapter);
		adapter.addAll(eventActivity);
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

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		}
	}

	/*
	 * Handle the event when changing orientation triggers restart of activity
	 * 
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void onCommentPost(View view){
		
		Posts post = new Posts();
		//Save in the post table
		ParseUser user = ParseUser.getCurrentUser();
		post.put("userPtr", user);
		JSONObject eventPtr = new JSONObject();
		try {
			eventPtr.put("__type", "Pointer");
			eventPtr.put("className", "Events");
			eventPtr.put("objectId", eventId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post.put("eventPtr", eventPtr);
		EditText postText = (EditText) findViewById(R.id.etPost);
		post.put("post", postText.getText().toString());
		try {
			post.save();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		
		//create entry in the activity table
 		EventActivity eActivity = new EventActivity();
 		String postId = post.getObjectId();
 		JSONObject postPtr = new JSONObject();
		try {
			postPtr.put("__type", "Pointer");
			postPtr.put("className", "Posts");
			postPtr.put("objectId", postId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		eActivity.put("postPtr", postPtr);
		eActivity.put("activityFrom", user);
		eActivity.put("eventPtr", eventPtr);
		eActivity.put("eventObj", eventId);
		try {
			eActivity.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Adapter.add(this.post) and refresh adapter
		List<EventActivity> eventActivity = new ArrayList<EventActivity>();
		ParseQuery<EventActivity> query = fetchEventActivityItems();

		try {
			eventActivity = query.find();
		} catch (ParseException e) {
			Log.d("ERROR", e.toString());
		}
		adapter.clear();
		adapter.addAll(eventActivity);
	}

}
