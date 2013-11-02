package com.hubdub.meetr.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.model.GraphUser;
import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.CustomArrayAdapter;
import com.hubdub.meetr.models.Events;
import com.parse.Parse;
import com.parse.ParseObject;

public class EventListActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	private CustomArrayAdapter adapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * Custom adapter serves up the Event feed for the current logged in
		 * user
		 */
		ParseObject.registerSubclass(Events.class);
		Parse.initialize(this, "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");
	
		adapter = new CustomArrayAdapter(this);
		listView = (ListView) findViewById(R.id.lvItems);
		listView.setAdapter(adapter);

		/*
		 * Listeners for events on the listview. TODO Need to find a cleaner way
		 * of adding these listeners
		 */
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long rowId) {
				Events event = (Events) adapter.getItemAtPosition(pos);
				Log.d("debug-position", event.getEventName());
				return false;
				// Do something here
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos,
					long rowId) {
				// TODO Auto-generated method stub
				Events event = (Events) adapter.getItemAtPosition(pos);
				Log.d("debug-position-2", event.getEventName());
				Intent i = new Intent(EventListActivity.this, EventDetailActivity.class);
				Bundle extras = new Bundle();
				extras.putString("EventName", event.getEventName());
				extras.putString("EventDate", event.getEventDate().toString());
				extras.putString("EventTime", event.getEventTime().toString());
				
				JSONArray guestList = new JSONArray();
				guestList = event.getGuestList();
				String results = "";
				/* This needs to be made into a function
				 * 
				 */
				if (guestList != null && guestList.length() > 0) {
					ArrayList<String> names = new ArrayList<String>();
					for (int j = 0; j < guestList.length(); j++) {
						  GraphUser user = null;
						try {
							user = (GraphUser)guestList.getJSONObject(j);
							names.add(user.getName());
							Log.d("output", names.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(user != null)
							names.add(user.getName());
					}
					results = TextUtils.join(", ", names);
				} else {
					results = "<No friends selected>";
				}
				extras.putString("GuestList", results);
				i.putExtras(extras);
				startActivity(i);

			}
		});

		listView.setAdapter(adapter);
	}

	/*
	 * Function to create the dummy data for testing TODO Remove this once we
	 * have the actual data hook up
	 */
	// public static ArrayList<Events> addEvents() {
	// ArrayList<Events> events = new ArrayList<Events>();
	//
	// /* 1. Form the query string
	// * 2. Send the query to parse
	// * 3. array of eventItems = (?) Response of Query
	// */
	// ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
	// query.whereEqualTo("User", ParseUser.getCurrentUser());
	// query.findInBackground(new FindCallback<ParseObject>() {
	// public void done(List<ParseObject> eventList, ParseException e) {
	// if (e == null) {
	// Log.d("Events", "Retrieved " + eventList.size() + " events");
	// } else {
	// Log.d("Events", "Error: " + e.getMessage());
	// }
	// }
	//
	// });
	//
	// events.add(new Events());
	// return events;
	// }

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
			composeEvent();
			return true;
		case R.id.action_camera:
			callCameraFragment();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * Helper functions for Menu/Action bar
	 */
	public void composeEvent() {
		Intent i = new Intent(this, ComposeActivity.class);
		i.putExtra("dummyApiKey", "q1b2f3j4o5t6l1d2");
		startActivityForResult(i, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			adapter.loadObjects();
			listView.setAdapter(adapter);
		}
	}

	public void callCameraFragment() {
		Intent i = new Intent(this, CameraActivity.class);
		startActivity(i);

		Toast toast = Toast.makeText(getApplicationContext(), "Add pictures",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

	}

	/*
	 * Handle the event when changing orientation triggers restart of activity
	 * Read: http://
	 * 
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}
