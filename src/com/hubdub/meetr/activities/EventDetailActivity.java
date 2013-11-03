package com.hubdub.meetr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.models.Events;

public class EventDetailActivity extends Activity {

	private TextView tvEventName;
	private TextView tvRsvp;
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
		
		
		tvEventName.setText(eventName);
//		tvRsvp.setText(event.getRsvp());
//		ivEventImage.setImageBitmap(event.getEventImageUrl());
		tvDescriptionBody.setText(eventDescription);
		tvDateBody.setText(date);
		tvTimeBody.setText(time);
		tvGuestsBody.setText(guestList);
//		tvVenueBody.setText();
		
	}

	private void setupViews() {
		tvEventName = (TextView) findViewById(R.id.tvEventName);
		tvRsvp = (TextView) findViewById(R.id.tvRsvp);
		ivEventImage = (ImageView) findViewById(R.id.ivEventImage);
		tvDescriptionBody = (TextView) findViewById(R.id.tvDescriptionBody);
		tvDateBody = (TextView) findViewById(R.id.tvDateBody);
		tvTimeBody = (TextView) findViewById(R.id.tvTimeBody);
		tvGuestsBody = (TextView) findViewById(R.id.tvGuestsBody);
		tvVenueBody = (TextView) findViewById(R.id.tvVenueBody);
	}
	
}
