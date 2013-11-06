package com.hubdub.meetr.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hubdub.meetr.R;
import com.hubdub.meetr.fragments.EventDetailFragment;
import com.hubdub.meetr.fragments.EventTimelineFragment;

public class EventDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail_frame);
		setupTabs();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab eventDetailTab = actionBar
			.newTab()
			.setText("Event details")
//			.setIcon(R.drawable.)
			.setTag("EventDetailFragment")
			.setTabListener(
				new FragmentTabListener<EventDetailFragment>(R.id.flForEventDetailTabs, this, "EventDetailFragment",
						EventDetailFragment.class));

		actionBar.addTab(eventDetailTab);
		actionBar.selectTab(eventDetailTab);

		Tab eventTimelineTab = actionBar
			.newTab()
			.setText("Event Timeline")
//			.setIcon(R.drawable.ic_mentions)
			.setTag("EventTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<EventTimelineFragment>(R.id.flForEventDetailTabs, this, "EventTimelineFragment",
			    		EventTimelineFragment.class));

		actionBar.addTab(eventTimelineTab);
	}
	
	public void onEditButtonClicked() {
		Intent i = new Intent(this, ComposeActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eventdetail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditButtonClicked();
			return true;
		case R.id.action_camera:
			callCameraFragment();
			return true;
		case android.R.id.home:
			onBackPressed();  //This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
		
}
