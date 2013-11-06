package com.hubdub.meetr.fragments;

import com.hubdub.meetr.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailFragment extends Fragment {

	private TextView tvEventName;
//	private TextView tvRsvp;
	private ImageView ivEventImage;
	private TextView tvDescriptionBody;
	private TextView tvDateBody;
	private TextView tvTimeBody;
    private TextView tvGuestsBody;
	private TextView tvVenueBody;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(com.hubdub.meetr.R.layout.activity_event_detail, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();
	
		Intent i = getActivity().getIntent();
		
		String eventName = i.getStringExtra("EventName");
		String eventDescription = i.getStringExtra("Description");
		Long eventDate = i.getLongExtra("EventDate", 0);
		Long eventTime = i.getLongExtra("EventTime", 0);
		String date = DateFormat.format("dd MMM, yyyy", eventDate).toString();
		String time = DateFormat.format("h:mm a", eventTime).toString();
		String guestList = i.getStringExtra("GuestList");
		String location = i.getStringExtra("Location");
		String locationQuery = location.replace("\n", "+").replace(" ", "+").replace(",", "+");
		String url = "http://maps.googleapis.com/maps/api/staticmap?center="
				+ locationQuery
				+ "zoom=13&size=400x120&maptype=roadmap&markers=color:red%7Caddress="
				+ locationQuery + "%7C&sensor=false";
		
		tvEventName.setText(eventName);
		tvDescriptionBody.setText(eventDescription);
		tvDateBody.setText(date);
		tvTimeBody.setText(time);
		tvGuestsBody.setText(guestList);
		tvVenueBody.setText(location);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		imageLoader.displayImage(url, ivEventImage);
	}
	
	private void setupViews() {
		tvEventName = (TextView) getView().findViewById(R.id.tvEventName);
//		tvRsvp = (TextView) findViewById(R.id.tvRsvp);
		tvDescriptionBody = (TextView) getView().findViewById(R.id.tvDescriptionBody);
		tvDateBody = (TextView) getView().findViewById(R.id.tvDateBody);
		tvTimeBody = (TextView) getView().findViewById(R.id.tvTimeBody);
		tvGuestsBody = (TextView) getView().findViewById(R.id.tvGuestsBody);
		tvVenueBody = (TextView) getView().findViewById(R.id.tvVenueBody);
		ivEventImage = (ImageView) getView().findViewById(R.id.ivEventImage);
	}
	
}
