package com.hubdub.meetr.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

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
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.hubdub.meetr.R;
import com.hubdub.meetr.fragments.DatePickerFragment;
import com.hubdub.meetr.fragments.TimePickerFragment;
import com.hubdub.meetr.fragments.TimePickerFragment.TimePickedListener;
import com.hubdub.meetr.models.Events;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class ComposeActivity extends FragmentActivity implements
		OnDateSetListener, TimePickedListener, LocationListener {

	private static final String APPLICATION_ID = "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU";
	private static final String CLIENT_KEY = "3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH";
	private EditText mEventNameInput;
	private Date eventDate;
	private Date eventTime;
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;
	private Button pickFriendsButton;
	private static final int PICK_FRIENDS_ACTIVITY = 1;
	private static final int PICK_PLACE_ACTIVITY = 2;
	static final int DATE_DIALOG_ID = 999;
	private JSONArray guestListArray = new JSONArray();
	private String results = new String();
	private String selectedString = new String();
	private TextView tvDescriptionBody;
	private Button btLocation;
	private Location lastKnownLocation;
	private LocationManager locationManager;
	private Location pickPlaceForLocationWhenSessionOpened = null;
	private boolean canGetLocation;
	private ArrayList <String> fbGuestList = new ArrayList<String>();

	private static final Location SAN_FRANCISCO_LOCATION = new Location("") {
		{
			setLatitude(37.7750);
			setLongitude(-122.4183);
		}
	};
	private static final int SEARCH_RADIUS_METERS = 1000*50; //50km
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
		
		ActionBar bar=getActionBar();
	    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5D79B4")));
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
		
		// Update the display every time we are started (this will be "no place selected" on first
        // run, or possibly details of a place if the activity is being re-created).
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

            // TODO format this better for when parts of the address are missing. Adds comma without any word before it.
            results = String.format("%s\n%s, %s, %s", selection.getName(), location.getStreet(), location.getCity(), location.getState());
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
		if (mEventNameInput.getText().length() > 0) {
			final Events event = new Events();
			final ParseUser currentUser = ParseUser.getCurrentUser();
			event.setEventName(mEventNameInput.getText().toString());
			event.setEventDescription(tvDescriptionBody.getText().toString());
			event.setEventDate(eventDate);
			event.setEventTime(eventTime);
			addSelectedFriends();
			event.setLocation(btLocation.getText().toString());
			event.setGuestList(guestListArray);
			event.setCurrentUser(currentUser);
			event.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					final String eventId = event.getObjectId();
					final String eventName = event.getEventName();

					JSONArray guestList = event.getGuestList();
					/* Setup the push service to call EventListActivity class */
					PushService.subscribe(getBaseContext(), "event_"+eventId, com.hubdub.meetr.activities.EventListActivity.class);
					
					for(int i=0; i<guestList.length(); i++) {
						try {
							/* Get facebook id for each guest */
							JSONObject guest = (JSONObject) guestList.get(i);
							String fbId = (String) guest.get("id");
							String fbName = (String) guest.get("name");
							fbGuestList.add(fbId);
							
							/* Craft the push notification */
							JSONObject data = new JSONObject ();
							data.put("action", "com.hubdub.meetr.activities.UPDATE_STATUS");
							data.put("title", "Meetr");
							data.put("eventId", eventId);
							/* Get the user name from current user */
							JSONObject userName = (JSONObject) currentUser.getJSONObject("profile");
							data.put("alert", userName.get("name") + "has invited you to " + eventName);
							
							/* Find the guests from our list and send out a push notification to each */
							ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
							pushQuery.whereEqualTo("fbId", fbId);
							ParsePush push = new ParsePush();
							push.setQuery(pushQuery);
							push.setData(data);
							push.sendInBackground();
														
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			try {
				fbGuestList.add(ParseUser.getCurrentUser().getJSONObject("profile").getString("facebookId"));
			} catch (JSONException e1) {
					e1.printStackTrace();
			}
			
			fbGuestList = new ArrayList<String>(new HashSet<String>(fbGuestList));
			event.setFbGuestList(fbGuestList);
			event.saveEventually();
			
			Log.d("DEBUG", "about to save event");
			
			// Instead of going back to the EventListActivity, we are going to
			// start a new activity that shows the newly created event
			Intent i = new Intent(ComposeActivity.this,
					EventDetailActivity.class);

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
			i.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);

			startActivity(i);
			// Need to close this activity and head back out.
			mEventNameInput.setText("");
			/* reset the global variable */
			MeetrApplication application = (MeetrApplication) getApplication();
			application.setSelectedUsers(null);
			finish();
		}
	}

	private void addSelectedFriends() {
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
		finish();
	}

	public void onPickLocationButtonSelected(View v) {
		// Intent i = new Intent(ComposeActivity.this, PickPlaceActivity.class);
		// startActivityForResult(i, PICK_PLACE_ACTIVITY );
		findCurrentLocation();
	}
	
	public Location getLocation() {
	    Location location = null;
		try {
	        locationManager = (LocationManager) this.getBaseContext().getSystemService(LOCATION_SERVICE);

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
//					lastKnownLocation = SAN_FRANCISCO_LOCATION;
					lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
				}
			}
			if (lastKnownLocation == null) {
				String model = android.os.Build.MODEL;
				if (model.equals("sdk") || model.equals("google_sdk")
						|| model.contains("x86") || model.contains("Google Apps")) {
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
			PickPlaceActivity.populateParameters(intent, location, null, SEARCH_RADIUS_METERS, false);

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
}
