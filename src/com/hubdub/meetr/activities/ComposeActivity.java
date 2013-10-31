package com.hubdub.meetr.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.activities.TimePickerFragment.TimePickedListener;
import com.hubdub.meetr.models.Events;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

@SuppressLint("SimpleDateFormat")
public class ComposeActivity extends FragmentActivity implements
		OnDateSetListener, TimePickedListener {

	private EditText mEventNameInput;
	private Date eventDate;
	private Date eventTime;
	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Parse Initialization */
		ParseObject.registerSubclass(Events.class);
		Parse.initialize(this, "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");

		setContentView(R.layout.activity_compose);
		mEventNameInput = (EditText) findViewById(R.id.eventName);
	}

	public void onEventCreateAction(View v) {
		if (mEventNameInput.getText().length() > 0) {
			Events event = new Events();
			ParseUser currentUser = ParseUser.getCurrentUser();
			event.setEventName(mEventNameInput.getText().toString());
			event.setEventDate(eventDate);
			event.setEventTime(eventTime);
			event.setCurrentUser(currentUser);
			event.saveEventually();
			// Instead of going back to the EventListActivity, we are going to
			// start a new activity that shows the newly created event
			Intent i = new Intent(ComposeActivity.this, EventDetailActivity.class);
			Bundle extras = new Bundle();
			extras.putString("EventName", mEventNameInput.getText().toString());
			extras.putString("EventDate", eventDate.toString());
			extras.putString("EventTime", eventTime.toString());
			i.putExtras(extras);
			startActivity(i);
			// Need to close this activity and head back out.
						mEventNameInput.setText("");
		}
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	/**
	 * //This is how you convert time from parse to local time
	 * SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); String parseDate =
	 * formatter.format(mEventDate.getTime());
	 * System.out.println(mEventDate.getTime());
	 * System.out.println(parseDate);
	 **/
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Log.w("DatePicker", "Date = " + year);
		((TextView) findViewById(R.id.tvDate)).setText("Date = "
				+ (monthOfYear + 1) + "-" + dayOfMonth + "-" + year);

		// This is how you convert the date set by the date picker to Parse
		// time.
		GregorianCalendar mEventDate = new GregorianCalendar(year, monthOfYear,
				dayOfMonth);
		eventDate = mEventDate.getTime();
	}

	@Override
	public void onTimePicked(Calendar time) {
		Log.w("TimePicker", "Time = " + DateFormat.format("h:mm a", time));
		((TextView) findViewById(R.id.tvTime)).setText(DateFormat.format(
				"h:mm a", time));
		eventTime = time.getTime();
	}

}