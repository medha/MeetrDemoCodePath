package com.hubdub.tutorials.activities;

import hubdub.tutorials.models.Event;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

import com.hubdub.tutorials.R;
import com.hubdub.tutorials.adapters.customArrayAdapter;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArrayList<Event> arrayOfEvents = addEvents();
		customArrayAdapter adapter = new customArrayAdapter(this, arrayOfEvents);
		ListView listView = (ListView) findViewById(com.hubdub.tutorials.R.id.lvItems);
		
		/* Listeners for events on the listview
		 * Need to find a cleaner way of adding these listeners
		 */
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long rowId) {
		    	Event event = (Event) adapter.getItemAtPosition(pos);
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
				Event event = (Event) adapter.getItemAtPosition(pos);
		    	Log.d("debug-position-2", event.getEventName());
				
			}
		});

		listView.setAdapter(adapter);
	}
	
	//Function to create the dummy data for testing
	public static ArrayList <Event> addEvents(){
		ArrayList <Event> events = new ArrayList<Event>();
		for (int i=0; i<20; i++){
			events.add(new Event());
		}
		return events;
	}
	
	/*
	 * Menu related stuff goes here
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()) {
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
	
	public void composeEvent() {
		Intent i = new Intent(this, composeActivity.class);
		Toast toast = Toast.makeText(getApplicationContext(), "Create a new Event", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

	}
	
	public void callCameraFragment(){
		
		Toast toast = Toast.makeText(getApplicationContext(), "Add pictures", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	    super.onConfigurationChanged(newConfig);
	}

}
