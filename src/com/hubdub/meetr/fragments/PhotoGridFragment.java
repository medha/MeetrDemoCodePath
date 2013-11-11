package com.hubdub.meetr.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.activities.ImageDisplayActivity;
import com.hubdub.meetr.models.Photos;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PhotoGridFragment extends Fragment {
	
	GridView gridView;
	List<Photos> photos = new ArrayList<Photos>();
	PhotoGridAdapter adapter;
	int start = 0;
	private String eventId;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(com.hubdub.meetr.R.layout.event_photo_grid, container, false);

		/* register parse object */
		ParseObject.registerSubclass(Photos.class);
		Parse.initialize(getActivity(), "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");

		Intent i = getActivity().getIntent();
		eventId = i.getStringExtra("EventId");
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView) getView().findViewById(R.id.gridview);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				 // Sending image id to FullScreenActivity
                Intent i = new Intent(getActivity(), ImageDisplayActivity.class);
                // passing array index
                Photos photo = adapter.getItem(position);
    			String url = photo.getParseFile("photo").getUrl();
                i.putExtra("url", url);
                startActivity(i);
			}
		});
		
		/* Initial load of photos */
		loadData();
	}
	
	/*
	 * Functon to Load data into gridview
	 */
	public void loadData() {
		
		ParseQuery<Photos> query = fetchEventPhotos();
			query.findInBackground(new FindCallback<Photos>(){
				@Override
				public void done(List<Photos> object, ParseException e) {
					photos = object;
					adapter = new PhotoGridAdapter(getActivity(), new ArrayList<Photos>());
					gridView.setAdapter(adapter);
					adapter.addAll(photos);
				}
			});
	}
	
	private ParseQuery<Photos> fetchEventPhotos() {
		/*
		 * Doing a join query here. Requesting all rows where the event is
		 * created by this user and also where this user is an invited guest.
		 */
		JSONObject eventPtr = new JSONObject();
		try {
			eventPtr.put("__type", "Pointer");
			eventPtr.put("className", "Events");
			eventPtr.put("objectId", eventId);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
		ParseQuery<Photos> query = new ParseQuery<Photos>("Photos");
		query.whereEqualTo("eventPtr", eventPtr);
		query.orderByDescending("createdAt");
		return query;
	}
	
	
	public class PhotoGridAdapter extends ArrayAdapter<Photos> {
		

		public PhotoGridAdapter(Context context, List<Photos> photos) {
			super(context, 0, photos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.simple_image_view, parent, false);
			} else {
				imageView = (ImageView) convertView;
				imageView.setImageResource(android.R.color.transparent);;
			}

			Photos photo = getItem(position);
			String url = photo.getParseFile("photo").getUrl();
			
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageLoader.displayImage(url, imageView);
		
			return imageView;
		}
	}

}
