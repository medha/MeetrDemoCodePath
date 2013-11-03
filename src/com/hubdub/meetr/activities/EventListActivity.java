package com.hubdub.meetr.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			composeEvent();
			return true;
		case R.id.action_camera:
			callCameraFragment();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		listView.setAdapter(new CustomArrayAdapter(this));
	}
	
	/*
	 * Functon to Load data into the listview
	 */
	public void loadData(){
		adapter = new CustomArrayAdapter(this);
		listView = (ListView) findViewById(R.id.lvItems);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long rowId) {
				//Events event = (Events) adapter.getItemAtPosition(pos);
				return false;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos,
					long rowId) {
				Events event = (Events) adapter.getItemAtPosition(pos);
				JSONArray guestList = event.getGuestList();				

				Bundle extras = new Bundle();
				extras.putString("EventName", event.getEventName());
				extras.putString("Description", event.getDescription());
				extras.putLong("EventDate", event.getEventDate().getTime());
				extras.putLong("EventTime", event.getEventTime().getTime());				
				extras.putString("GuestList", getStrGuestList(guestList));

				Intent i = new Intent(EventListActivity.this, EventDetailActivity.class);
				i.putExtras(extras);
				startActivity(i);
			}
		});

		listView.setAdapter(adapter);
	}

	
	/*
	 * Helper Function for the adapter
	 */
	public String getStrGuestList(JSONArray guestList){
		JSONObject childJSONObject;
		ArrayList<String> names = new ArrayList<String>();
		String results = "";
		if (guestList != null && guestList.length() > 0) {
			for (int j = 0; j < guestList.length(); j++) {
				try {
					childJSONObject = guestList.getJSONObject(j);
					names.add(childJSONObject.getString("name"));
					Log.d("output", names.toString());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
			results = TextUtils.join(", ", names);
		}		
		return results;
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

	/* Handle the event when changing orientation triggers restart of activity
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}
