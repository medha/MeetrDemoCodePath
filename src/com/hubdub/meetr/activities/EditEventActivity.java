package com.hubdub.meetr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hubdub.meetr.R;
import com.hubdub.meetr.fragments.ConvergeTimelineFragment;

public class EditEventActivity extends ComposeActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
	
	@Override
	public void onEventCreateAction(View v) {
		if (mEventNameInput.getText().length() > 0) {
			
			// start a new activity that shows the newly created event
			Intent i = new Intent(EditEventActivity.this,
					ConvergeTimelineFragment.class);

			// Additional information being sent across
			Bundle extras = new Bundle();
			extras.putString("EventName", mEventNameInput.getText().toString());
			extras.putLong("EventDate", eventDate.getTime());
			extras.putLong("EventTime", eventTime.getTime());
			extras.putString("GuestList", results);
			extras.putString("Description", tvDescriptionBody.getText()
					.toString());
			extras.putString("Location", btLocation.getText().toString());
			i.putExtras(extras);
			startActivity(i);
			// Need to close this activity and head back out.
			mEventNameInput.setText("");
			/* reset the global variable */
			MeetrApplication application = (MeetrApplication) getApplication();
			application.setSelectedUsers(null);
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eventcreate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_create:
			onDoneButtonClicked();
			return true;
		case android.R.id.home:
			onBackPressed(); // This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onDoneButtonClicked() {
		// We just store our selection in the Application for other activities
		// to look at.
		onEventCreateAction(this.getCurrentFocus());
		finish();
	}
	
	
}
