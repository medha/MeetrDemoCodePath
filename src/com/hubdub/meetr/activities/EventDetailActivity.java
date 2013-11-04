package com.hubdub.meetr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hubdub.meetr.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class EventDetailActivity extends Activity {

	private TextView tvEventName;
//	private TextView tvRsvp;
	private ImageView ivEventImage;
	private TextView tvDescriptionBody;
	private TextView tvDateBody;
	private TextView tvTimeBody;
    private TextView tvGuestsBody;
	private TextView tvVenueBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_event_detail);
		
		setupViews();
		
		Intent i = getIntent();
		
		String eventName = i.getStringExtra("EventName");
		String eventDescription = i.getStringExtra("Description");
		Long eventDate = i.getLongExtra("EventDate", 0);
		Long eventTime = i.getLongExtra("EventTime", 0);
		String date = DateFormat.format("dd MMM, yyyy", eventDate).toString();
		String time = DateFormat.format("h:mm a", eventTime).toString();
		String guestList = getIntent().getStringExtra("GuestList");
		String location = i.getStringExtra("Location");
		String locationQuery = location.replace("\n", "+").replace(" ", "+").replace(",", "+");
		String url = "http://maps.googleapis.com/maps/api/staticmap?center="
				+ locationQuery
				+ "zoom=13&size=400x220&maptype=roadmap&markers=color:red%7Caddress="
				+ locationQuery + "%7C&sensor=false";
		
		tvEventName.setText(eventName);
		tvDescriptionBody.setText(eventDescription);
		tvDateBody.setText(date);
		tvTimeBody.setText(time);
		tvGuestsBody.setText(guestList);
		tvVenueBody.setText(location);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		imageLoader.displayImage(url, ivEventImage);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setupViews() {
		tvEventName = (TextView) findViewById(R.id.tvEventName);
//		tvRsvp = (TextView) findViewById(R.id.tvRsvp);
		tvDescriptionBody = (TextView) findViewById(R.id.tvDescriptionBody);
		tvDateBody = (TextView) findViewById(R.id.tvDateBody);
		tvTimeBody = (TextView) findViewById(R.id.tvTimeBody);
		tvGuestsBody = (TextView) findViewById(R.id.tvGuestsBody);
		tvVenueBody = (TextView) findViewById(R.id.tvVenueBody);
		ivEventImage = (ImageView) findViewById(R.id.ivEventImage);
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
