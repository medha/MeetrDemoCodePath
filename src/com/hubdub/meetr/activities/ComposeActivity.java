package com.hubdub.meetr.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.hubdub.meetr.activities.PickFriendsActivity;
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
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;
	private Button pickFriendsButton;
    private TextView resultsTextView;
    private static final int PICK_FRIENDS_ACTIVITY = 1;
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

		/*
		 * Source: Facebook friend picker sample code from the sample
		 * application
		 */
		resultsTextView = (TextView) findViewById(R.id.resultsTextView);
		pickFriendsButton = (Button) findViewById(R.id.pickFriendsButton);
		pickFriendsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPickFriends();
			}
		});

		lifecycleHelper = new UiLifecycleHelper(this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChanged(session, state, exception);
					}
				});
		lifecycleHelper.onCreate(savedInstanceState);

		ensureOpenSession();
	}
	
	/* show the friends selected by the friend picker
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	protected void onStart() {
		super.onStart();

		// Update the display every time we are started.
		displaySelectedFriends(RESULT_OK);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_FRIENDS_ACTIVITY:
			displaySelectedFriends(resultCode);
			break;
		default:
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
			break;
		}
	}
	
	private void displaySelectedFriends(int resultCode) {
		String results = "";
        MeetrApplication application = (MeetrApplication) getApplication();
		Collection<GraphUser> selection = application.getSelectedUsers();
		if (selection != null && selection.size() > 0) {
			ArrayList<String> names = new ArrayList<String>();
			for (GraphUser user : selection) {
				names.add(user.getName());
			}
			results = TextUtils.join(", ", names);
		} else {
			results = "<No friends selected>";
		}

		resultsTextView.setText(results);
	}
	
	/* Re-use open session to facebook for the friend picker
	 * Better utilization of the already open session. 
	 */
	private boolean ensureOpenSession() {
        if (Session.getActiveSession() == null ||
                !Session.getActiveSession().isOpened()) {
            Session.openActiveSession(this, true, new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    onSessionStateChanged(session, state, exception);
                }
            });
            return false;
        }
        return true;
    }
	
	  private void onSessionStateChanged(Session session, SessionState state, Exception exception) {
	        if (pickFriendsWhenSessionOpened && state.isOpened()) {
	            pickFriendsWhenSessionOpened = false;
	            startPickFriendsActivity();
	        }
	    }
	  
	   private void onClickPickFriends() {
	        startPickFriendsActivity();
	    }
	   
	    private void startPickFriendsActivity() {
	        if (ensureOpenSession()) {
	        	MeetrApplication application = (MeetrApplication) getApplication();
	    		application.setSelectedUsers(null);

	            Intent intent = new Intent(this, PickFriendsActivity.class);
	            // Note: The following line is optional, as multi-select behavior is the default for
	            // FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
	            // friend picker if single-select functionality was desired, or if a different user ID was
	            // desired (for instance, to see friends of a friend).
	            PickFriendsActivity.populateParameters(intent, null, true, true);
	            startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
	        } else {
	            pickFriendsWhenSessionOpened = true;
	        }
	    }

	public void createEvent(View v) {
		if (mEventNameInput.getText().length() > 0) {
			Events event = new Events();
			ParseUser currentUser = ParseUser.getCurrentUser();
			event.setEventName(mEventNameInput.getText().toString());
			event.setEventDate(eventDate);
			event.setEventTime(eventTime);
			event.setCurrentUser(currentUser);
			event.saveEventually();
			// Need to close this activity and head back out.
			mEventNameInput.setText("");
			setResult(RESULT_OK);
			finish();
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

		/**
		 * //This is how you convert time from parse to local time
		 * SimpleDateFormat formatter = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); String parseDate =
		 * formatter.format(mEventDate.getTime());
		 * System.out.println(mEventDate.getTime());
		 * System.out.println(parseDate);
		 **/

	}

	@Override
	public void onTimePicked(Calendar time) {
		Log.w("TimePicker", "Time = " + DateFormat.format("h:mm a", time));
		((TextView) findViewById(R.id.tvTime)).setText(DateFormat.format(
				"h:mm a", time));
		eventTime = time.getTime();
	}
}