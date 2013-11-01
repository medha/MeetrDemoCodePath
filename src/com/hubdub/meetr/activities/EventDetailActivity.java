package com.hubdub.meetr.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubdub.meetr.R;

public class EventDetailActivity extends Activity {

	private TextView tvEventName;
	private TextView tvRsvp;
	private ImageView ivEventImage;
	private TextView tvDescriptionBody;
	private TextView tvDateBody;
	private TextView tvTimeBody;
//	private TextView tvGuestsBody;
	private TextView tvVenueBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_event_detail);
		
		setupViews();
		
		String eventName = getIntent().getStringExtra("EventName");
		String eventDate = getIntent().getStringExtra("EventDate");
		String eventTime = getIntent().getStringExtra("EventTime");
		
		tvEventName.setText(eventName);
//		tvRsvp.setText(event.getRsvp());
//		ivEventImage.setImageBitmap(event.getEventImageUrl());
//		tvDescriptionBody.setText(event.getDescription());
		tvDateBody.setText(eventDate);
		tvTimeBody.setText(eventTime);
		//tvGuestsBody.setText(event.getGuestList());
//		tvVenueBody.setText();
		
	}

	private void setupViews() {
		tvEventName = (TextView) findViewById(R.id.tvEventName);
		tvRsvp = (TextView) findViewById(R.id.tvRsvp);
		ivEventImage = (ImageView) findViewById(R.id.ivEventImage);
		tvDescriptionBody = (TextView) findViewById(R.id.tvDescriptionBody);
		tvDateBody = (TextView) findViewById(R.id.tvDateBody);
		tvTimeBody = (TextView) findViewById(R.id.tvTimeBody);
//		tvGuestsBody = (TextView) findViewById(R.id.tvGuestsBody);
		tvVenueBody = (TextView) findViewById(R.id.tvVenueBody);
	}
	
}
