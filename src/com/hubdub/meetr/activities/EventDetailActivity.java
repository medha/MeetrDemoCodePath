package com.hubdub.meetr.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();  //This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
