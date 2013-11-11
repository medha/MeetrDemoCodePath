package com.hubdub.meetr.fragments;

import java.io.FileNotFoundException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hubdub.meetr.R;
import com.hubdub.meetr.activities.EditEventActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class EventDetailFragment extends Fragment {

	private TextView tvEventName;
	private ImageView ivEventImage;
	private TextView tvDescriptionBody;
	private TextView tvDateBody;
	private TextView tvTimeBody;
	private TextView tvGuestsBody;
	private TextView tvVenueBody;
	private int REQUEST_CODE = 1;
	private String eventName;
	private String eventDescription;
	private long eventDate;
	private long eventTime;
	private String date;
	private String time;
	private String guestList;
	private String location;
	private String eventId;
	private String month;
	private TextView tvMonthBody;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(
				com.hubdub.meetr.R.layout.activity_event_detail, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();

		setHasOptionsMenu(true);

		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_converge_placeholder, new ConvergeTimelineFragment(), "ConvergeTimelineFragment");
		ft.commit();

		Intent i = getActivity().getIntent();

		eventId = i.getStringExtra("EventId");
		eventName = i.getStringExtra("EventName");
		eventDescription = i.getStringExtra("Description");
		eventDate = i.getLongExtra("EventDate", 0);
		eventTime = i.getLongExtra("EventTime", 0);
		
		
		month = DateFormat.format("MMM", eventDate).toString().toUpperCase();
		date = DateFormat.format("dd", eventDate).toString();
		time = DateFormat.format("h:mm a", eventTime).toString();
		
		guestList = i.getStringExtra("GuestList");
		location = i.getStringExtra("Location");
		if (location != null) {
			String locationQuery = location.replace("\n", "+")
					.replace(" ", "+").replace(",", "+");
			String url = "http://maps.googleapis.com/maps/api/staticmap?center="
					+ locationQuery
					+ "zoom=13&size=400x120&maptype=roadmap&markers=color:red%7Caddress="
					+ locationQuery + "%7C&sensor=false";
			ivEventImage.setVisibility(View.VISIBLE);

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getActivity()).build();
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
			
			imageLoader.displayImage(url, ivEventImage);
			
			String[] locationSplit = location.split("\n");
			tvVenueBody.setText(locationSplit[0] + ", " + locationSplit[1]);
		} else {
			tvVenueBody.setText("No location set");
			ivEventImage.setVisibility(View.GONE);
		}

		tvEventName.setText(eventName);
		tvDescriptionBody.setText(eventDescription);
		tvMonthBody.setText(month);
		tvDateBody.setText(date);
		tvTimeBody.setText(time);
		tvGuestsBody.setText(guestList);
	}

	private void setupViews() {
		tvEventName = (TextView) getView().findViewById(R.id.tvEventName);
		tvDescriptionBody = (TextView) getView().findViewById(
				R.id.tvDescriptionBody);
		tvDateBody = (TextView) getView().findViewById(R.id.tvDateBody);
		tvMonthBody = (TextView) getView().findViewById(R.id.tvMonth);
		tvTimeBody = (TextView) getView().findViewById(R.id.tvTimeBody);
		tvGuestsBody = (TextView) getView().findViewById(R.id.tvGuestsBody);
		tvVenueBody = (TextView) getView().findViewById(R.id.tvVenueBody);
		ivEventImage = (ImageView) getView().findViewById(R.id.ivEventImage);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.eventdetail, menu);
		MenuItem mi = menu.findItem(R.id.action_camera);
		if (mi != null)
			mi.setVisible(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditButtonClicked();
			return true;
		case android.R.id.home:
			getActivity().onBackPressed(); // This should be compatible with API
											// 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onEditButtonClicked() {
		Intent i = new Intent(getActivity(), EditEventActivity.class);
		Bundle extras = new Bundle();
		extras.putString("EventName", eventName);
		extras.putLong("EventDate", eventDate);
		extras.putLong("EventTime", eventTime);
		extras.putString("GuestList", guestList);
		extras.putString("Description", eventDescription);
		extras.putString("Location", location);
		extras.putString("EventId", eventId);
		i.putExtras(extras);
		startActivityForResult(i, REQUEST_CODE);
	}

	// FirstActivity, time to handle the result of the sub-activity
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == FragmentActivity.RESULT_OK && requestCode == REQUEST_CODE) {

			FragmentManager fm = getActivity().getSupportFragmentManager();
			Fragment fragment = fm.findFragmentById(R.id.fragment_converge_placeholder);
			((ConvergeTimelineFragment) fragment).loadData();
			
		}
	}

}
