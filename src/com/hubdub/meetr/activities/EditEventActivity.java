package com.hubdub.meetr.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.ConvergeTimelineAdapter;
import com.hubdub.meetr.fragments.ConvergeTimelineFragment;
import com.hubdub.meetr.fragments.DatePickerFragment;
import com.hubdub.meetr.fragments.DatePickerFragment.DatePickedListener;
import com.hubdub.meetr.fragments.TimePickerFragment;
import com.hubdub.meetr.fragments.TimePickerFragment.TimePickedListener;
import com.hubdub.meetr.models.EventActivity;
import com.hubdub.meetr.models.Events;
import com.hubdub.meetr.models.Suggestions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class EditEventActivity extends FragmentActivity implements
		OnDateSetListener, TimePickedListener, LocationListener, DatePickedListener {

	private static final String APPLICATION_ID = "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU";
	private static final String CLIENT_KEY = "3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH";
	protected EditText mEventNameInput;
	protected Date eventDate;
	protected Date eventTime;
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;
	private Button pickFriendsButton;
	private static final int PICK_FRIENDS_ACTIVITY = 1;
	private static final int PICK_PLACE_ACTIVITY = 2;
	static final int DATE_DIALOG_ID = 999;
	protected JSONArray guestListArray = new JSONArray();
	protected String results = new String();
	private String selectedString = new String();
	private TextView tvDescriptionBody;
	private Button btLocation;
	private Location lastKnownLocation;
	private LocationManager locationManager;
	private Location pickPlaceForLocationWhenSessionOpened = null;
	private boolean canGetLocation;
	protected ArrayList<String> fbGuestList = new ArrayList<String>();
	private String mEventName;
	private String mEventDescription;
	private long mEventDate;
	private long mEventTime;
	private String mDate;
	private String mTime;
	private String mGuestList;
	private String mLocation;
	private EditText etEventName;
	private Button btnChangeDate;
	private Button btnChangeTime;
	private String mEventId;
	private ConvergeTimelineAdapter adapter;
	private ListView listView;

	private static final Location SAN_FRANCISCO_LOCATION = new Location("") {
		{
			setLatitude(37.7750);
			setLongitude(-122.4183);
		}
	};
	private static final int SEARCH_RADIUS_METERS = 1000 * 50; // 50km
	private static final long MIN_TIME_BW_UPDATES = 10;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Parse Initialization */
		ParseObject.registerSubclass(Events.class);
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

		setContentView(R.layout.activity_compose);

		mEventNameInput = (EditText) findViewById(R.id.tvEventName);
		tvDescriptionBody = (TextView) findViewById(R.id.tvDescriptionBody);
		pickFriendsButton = (Button) findViewById(R.id.pickFriendsButton);
		btLocation = (Button) findViewById(R.id.btLocation);

		Intent prevArgs = getIntent();
		Bundle args = prevArgs.getExtras();

		// get values from Event Detail Fragment
		mEventName = args.getString("EventName");
		mEventDescription = args.getString("Description");
		mEventDate = args.getLong("EventDate", 0);
		mEventTime = args.getLong("EventTime", 0);
		mDate = DateFormat.format("MMM dd", mEventDate).toString();
		mTime = DateFormat.format("h:mm a", mEventTime).toString();
		mGuestList = args.getString("GuestList");
		mLocation = args.getString("Location");
		mEventId = args.getString("EventId");

		setupViews();

		// pre-populate the views with the event details
		etEventName.setText(mEventName);
		tvDescriptionBody.setText(mEventDescription);
		btnChangeDate.setText(mDate);
		btnChangeTime.setText(mTime);
		pickFriendsButton.setText(mGuestList);
		btLocation.setText(mLocation);

		/*
		 * Source: Facebook friend picker sample code from the sample
		 * application
		 */
		lifecycleHelper = new UiLifecycleHelper(this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChanged(session, state, exception);
					}
				});
		lifecycleHelper.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ensureOpenSession();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5D79B4")));
	}

	public void setupViews() {
		etEventName = (EditText) findViewById(R.id.tvEventName);
		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
		btnChangeTime = (Button) findViewById(R.id.btnChangeTime);
		btLocation = (Button) findViewById(R.id.btLocation);
		pickFriendsButton = (Button) findViewById(R.id.pickFriendsButton);
		pickFriendsButton.setVisibility(View.GONE);
		tvDescriptionBody = (EditText) findViewById(R.id.tvDescriptionBody);
	}

	/*
	 * show the friends selected by the friend picker (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	protected void onStart() {
		super.onStart();

		// Update the display every time we are started.
		displaySelectedFriends(RESULT_OK);

		// Update the display every time we are started (this will be
		// "no place selected" on first
		// run, or possibly details of a place if the activity is being
		// re-created).
		displaySelectedPlace(RESULT_OK);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lifecycleHelper.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		lifecycleHelper.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		lifecycleHelper.onResume();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		lifecycleHelper.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PICK_FRIENDS_ACTIVITY:
			displaySelectedFriends(resultCode);
			break;
		case PICK_PLACE_ACTIVITY:
			displaySelectedPlace(resultCode);
			break;
		default:
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
			break;
		}
	}

	private void displaySelectedFriends(int resultCode) {
		/*
		 * get the global application context to save the results from facebook
		 * friend picker.
		 */
		MeetrApplication application = (MeetrApplication) getApplication();
		Collection<GraphUser> selection = application.getSelectedUsers();

		if (selection != null && selection.size() > 0) {
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> selectedStr = new ArrayList<String>();

			for (GraphUser user : selection) {
				names.add(user.getName());
				selectedStr.add(user.getId());
				fbGuestList.add(user.getId());
			}
			results = TextUtils.join(", ", names);
			selectedString = TextUtils.join(", ", selectedStr);
			pickFriendsButton.setText(selection.size() + " Friends selected");
		} else {
			results = "Add friends";
			pickFriendsButton.setText("Add friends");
		}
	}

	private void displaySelectedPlace(int resultCode) {
		String results = "";
		MeetrApplication application = (MeetrApplication) getApplication();

		GraphPlace selection = application.getSelectedPlace();
		if (selection != null) {
			GraphLocation location = selection.getLocation();

			// TODO format this better for when parts of the address are
			// missing. Adds comma without any word before it.
			results = String.format("%s\n%s, %s, %s", selection.getName(),
					location.getStreet(), location.getCity(),
					location.getState());
		} else {
			results = "Pick a location";
		}

		btLocation.setText(results);
	}

	/*
	 * Re-use open session to facebook for the friend picker Better utilization
	 * of the already open session.
	 */
	private boolean ensureOpenSession() {
		if (Session.getActiveSession() == null
				|| !Session.getActiveSession().isOpened()) {
			Session.openActiveSession(this, true, new Session.StatusCallback() {
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					onSessionStateChanged(session, state, exception);
				}
			});
			return false;
		}
		return true;
	}

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (pickFriendsWhenSessionOpened && state.isOpened()) {
			pickFriendsWhenSessionOpened = false;
			startPickFriendsActivity();
		}

		if (pickPlaceForLocationWhenSessionOpened != null && state.isOpened()) {
			Location location = pickPlaceForLocationWhenSessionOpened;
			pickPlaceForLocationWhenSessionOpened = null;
			startPickPlaceActivity(location);
		}
	}

	public void onClickPickFriends(View v) {
		startPickFriendsActivity();
	}

	private void startPickFriendsActivity() {
		if (ensureOpenSession()) {
			Intent intent = new Intent(this, PickFriendsActivity.class);
			// Note: The following line is optional, as multi-select behavior is
			// the default for
			// FriendPickerFragment. It is here to demonstrate how parameters
			// could be passed to the
			// friend picker if single-select functionality was desired, or if a
			// different user ID was
			// desired (for instance, to see friends of a friend).
			PickFriendsActivity.populateParameters(intent, null, true, false,
					selectedString);
			startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
		} else {
			pickFriendsWhenSessionOpened = true;
		}
	}

	public void onEventCreateAction(View v) {

		final Intent i = new Intent();
		Bundle extras = new Bundle();

		final ParseUser currentUser = ParseUser.getCurrentUser();
		
		/* Populate the suggestion snapshot - this will replace the event details when the 
		 * vote count reaches majority
		 */
		final Suggestions suggestion = new Suggestions();
		
		
		suggestion.setCurrentUser(currentUser);

		String name;
		try {
			name = (String) currentUser.getJSONObject("profile").get("name");
			extras.putString("name", name);
		} catch (JSONException e) {
			Log.d("ERROR", e.toString());
		}

		if ((mEventNameInput != null)
				&& !mEventNameInput.getText().toString().equals(mEventName)) {
			extras.putString("EventName", mEventNameInput.getText().toString());
			suggestion.setEventName(mEventNameInput.getText().toString());
		} else {
			suggestion.setEventName(mEventName);
		}
		if ((eventDate != null) && !eventDate.equals(mEventDate)) {
			extras.putLong("EventDate", eventDate.getTime());
			suggestion.setEventDate(eventDate);
		} else {
			suggestion.setEventDate(new Date(mEventDate));
		}
		if ((eventTime != null) && !eventTime.equals(mTime)) {
			extras.putLong("EventTime", eventTime.getTime());
			suggestion.setEventTime(eventTime);
		} else {
			suggestion.setEventTime(new Date(mEventTime));
		}
		if ((tvDescriptionBody != null)
				&& !tvDescriptionBody.getText().toString()
						.equals(mEventDescription)) {
			extras.putString("Description", tvDescriptionBody.getText()
					.toString());
			suggestion.setEventDescription(tvDescriptionBody.getText().toString());
		} else {
			suggestion.setEventDescription(mEventDescription);
		}
		if ((btLocation != null)
				&& !btLocation.getText().toString().equals(mLocation)) {
			extras.putString("Location", btLocation.getText().toString());
			System.out.println(btLocation.getText().toString());
			suggestion.setLocation(btLocation.getText().toString());
		} else {
			suggestion.setLocation(mLocation);
		}
		i.putExtras(extras);
		
		suggestion.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				// create entry in the activity table
				EventActivity eActivity = new EventActivity();
				String suggestionId = suggestion.getObjectId();
		
				JSONObject suggestionPtr = new JSONObject();
				try {
					suggestionPtr.put("__type", "Pointer");
					suggestionPtr.put("className", "Suggestions");
					suggestionPtr.put("objectId", suggestionId);
				} catch (JSONException e1) {
					Log.d("ERROR", e1.getMessage());
				}
				
				JSONObject eventPtr = new JSONObject();
				try {
					eventPtr.put("__type", "Pointer");
					eventPtr.put("className", "Events");
					eventPtr.put("objectId", mEventId);
				} catch (JSONException e1) {
					Log.d("ERROR", e1.getMessage());
				}
				
				try {
					eActivity.put("suggestionPtr", suggestionPtr);
					eActivity.put("activityFrom", currentUser);
					eActivity.put("eventPtr", eventPtr);
					eActivity.put("eventObj", mEventId);
					eActivity.put("activityType", "suggestion");
					eActivity.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							
							setResult(RESULT_OK, i);
							// Need to close this activity and head back out.
							mEventNameInput.setText("");
							/* reset the global variable */
							MeetrApplication application = (MeetrApplication) getApplication();
							application.setSelectedUsers(null);
							finish();
						}
					});
				} catch (IllegalArgumentException e1) {
					Log.d("ERROR", e1.getMessage());
				}
			}

		});
	}

	protected void addSelectedFriends() {
		/*
		 * get the global application context to save the results from facebook
		 * friend picker.
		 */
		MeetrApplication application = (MeetrApplication) getApplication();
		Collection<GraphUser> selection = application.getSelectedUsers();

		if (selection != null && selection.size() > 0) {
			for (GraphUser user : selection) {
				guestListArray.put(user.getInnerJSONObject());
			}
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
	 * //This is how you convert time from parse to local time SimpleDateFormat
	 * formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); String
	 * parseDate = formatter.format(mEventDate.getTime());
	 * System.out.println(mEventDate.getTime()); System.out.println(parseDate);
	 **/
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Log.w("DatePicker", "Date = " + year);
		((TextView) findViewById(R.id.btnChangeDate)).setText("Date: "
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
		((TextView) findViewById(R.id.btnChangeTime)).setText("Time: "
				+ (DateFormat.format("h:mm a", time)));
		eventTime = time.getTime();
	}

	public void onDoneButtonClicked() {
		// We just store our selection in the Application for other activities
		// to look at.
		onEventCreateAction(this.getCurrentFocus());
	}

	public void onPickLocationButtonSelected(View v) {
		// Intent i = new Intent(ComposeActivity.this, PickPlaceActivity.class);
		// startActivityForResult(i, PICK_PLACE_ACTIVITY );
		findCurrentLocation();
	}

	public Location getLocation() {
		Location location = null;
		try {
			locationManager = (LocationManager) this.getBaseContext()
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				double latitude;
				double longitude;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	private void findCurrentLocation() {
		try {
			Location location = getLocation();

			if (location != null) {
				lastKnownLocation = location;
			}

			if (lastKnownLocation == null) {
				Criteria criteria = new Criteria();
				String bestProvider = locationManager.getBestProvider(criteria,
						false);
				if (bestProvider != null) {
					// lastKnownLocation = SAN_FRANCISCO_LOCATION;
					lastKnownLocation = locationManager
							.getLastKnownLocation(bestProvider);
				}
			}
			if (lastKnownLocation == null) {
				String model = android.os.Build.MODEL;
				if (model.equals("sdk") || model.equals("google_sdk")
						|| model.contains("x86")
						|| model.contains("Google Apps")) {
					// Looks like they are on an emulator, pretend we're in
					// Paris if we don't have a
					// location set.
					lastKnownLocation = SAN_FRANCISCO_LOCATION;
				} else {
					onError(new Exception(getString(R.string.no_location)));
					return;
				}
			}
			startPickPlaceActivity(lastKnownLocation);
		} catch (Exception ex) {
			onError(ex);
		}
	}

	private void startPickPlaceActivity(Location location) {
		if (ensureOpenSession()) {
			MeetrApplication application = (MeetrApplication) getApplication();
			application.setSelectedPlace(null);

			Intent intent = new Intent(this, PickPlaceActivity.class);
			PickPlaceActivity.populateParameters(intent, location, null,
					SEARCH_RADIUS_METERS, false);

			startActivityForResult(intent, PICK_PLACE_ACTIVITY);
		} else {
			pickPlaceForLocationWhenSessionOpened = location;
		}
	}

	private void onError(Exception exception) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Error").setMessage(exception.getMessage())
				.setPositiveButton("OK", null);
		builder.show();
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

	@Override
	public void onLocationChanged(Location location) {
		lastKnownLocation = location;

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	public Date setDateAndTime(Date mEventDate, Date mEventTime) {
		if (mEventDate != null && mEventTime != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(mEventDate);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			cal.setTime(mEventTime);
			int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			cal.set(year, month, day, hourOfDay, minute);
			return cal.getTime();
		} else
			return null;
	}
}