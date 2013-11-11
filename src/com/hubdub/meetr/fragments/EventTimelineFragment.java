package com.hubdub.meetr.fragments;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.EventTimeLnAdapter;
import com.hubdub.meetr.models.EventActivity;
import com.hubdub.meetr.models.Photos;
import com.hubdub.meetr.models.Posts;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EventTimelineFragment extends Fragment {
	private EventTimeLnAdapter adapter;
	private ListView listView;
	private String eventId;
	List<EventActivity> eventActivity = new ArrayList<EventActivity>();
	private static final int CAMERA_REQUEST = 1888; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_timeline, container, false);
		
		/*
		 * Custom adapter serves up the Event feed for the current logged in
		 * user
		 */
		ParseObject.registerSubclass(EventActivity.class);
		ParseObject.registerSubclass(Posts.class);
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
		

		listView = (ListView) getView().findViewById(R.id.lvItems);
		ImageButton submit_button = (ImageButton) getView().findViewById(R.id.submit_button);
		
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCommentPost(v);
			}
		});
		
		loadData();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.timeline, menu);
	   MenuItem mi =  menu.findItem(R.id.action_edit);
	   if ( mi != null) mi.setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_camera:
			callCameraFragment();
			return true;
		case android.R.id.home:
			getActivity().onBackPressed();  //This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void callCameraFragment() {
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_REQUEST); 
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {  
		if (requestCode == CAMERA_REQUEST && resultCode == FragmentActivity.RESULT_OK) {  

    		Toast toast = Toast.makeText(getActivity(), "Uploading...", Toast.LENGTH_LONG);
    		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    		toast.show();
            
            final Photos photos = new Photos();
    		final ParseUser user = ParseUser.getCurrentUser();
    		photos.put("userPtr", user);

    		final JSONObject eventPtr = new JSONObject();
    		try {
    			eventPtr.put("__type", "Pointer");
    			eventPtr.put("className", "Events");
    			eventPtr.put("objectId", eventId);
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    		photos.put("eventPtr", eventPtr);
    		photos.put("eventIdStr", eventId);
    		
    		Bitmap photo = (Bitmap) intent.getExtras().get("data");
    		
    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
    		byte[] byteArray = stream.toByteArray();
    		
    		ParseFile photoFile = new ParseFile("photo.png", byteArray);
    		photoFile.saveInBackground();
    		
    		photos.put("photo", photoFile);
    		photos.saveInBackground(new SaveCallback(){

    			@Override
    			public void done(ParseException e) {
    				//create entry in the activity table
    		 		EventActivity eActivity = new EventActivity();
    		 		String photoId = photos.getObjectId();
    		 		JSONObject photoPtr = new JSONObject();
    				try {
    					photoPtr.put("__type", "Pointer");
    					photoPtr.put("className", "Photos");
    					photoPtr.put("objectId", photoId);
    				} catch (JSONException e1) {
    					e1.printStackTrace();
    				}
    				eActivity.put("photoPtr", photoPtr);
    				eActivity.put("activityFrom", user);
    				eActivity.put("eventPtr", eventPtr);
    				eActivity.put("eventObj", eventId);		
    				eActivity.put("activityType", "photo");
    			
    			eActivity.saveInBackground(new SaveCallback(){
    				@Override
    				public void done(ParseException e) {
    					ParseQuery<EventActivity> query = fetchEventActivityItems();
    					query.findInBackground(new FindCallback<EventActivity>(){

    						@Override
    						public void done(List<EventActivity> objects,
    								ParseException e) {
    							eventActivity = objects;
    							adapter.clear();
    							adapter.addAll(eventActivity);
    							System.out.println("Added items to adapter: " + eventActivity.size());
    							listView.setAdapter(adapter);
    							System.out.println("Notifying data set changed.");
    							adapter.notifyDataSetChanged();
    						}
    						
    					});

    				}
    				});
    			}
    			
    		});
        }  
    } 
	
	private ParseQuery<EventActivity> fetchEventActivityItems() {
		/*
		 * Doing a join query here. Requesting all rows where the event is
		 * created by this user and also where this user is an invited guest.
		 */
		ParseQuery<EventActivity> query = new ParseQuery<EventActivity>("Activity");
		query.whereEqualTo("eventObj", eventId);
		query.include("activityFrom");
		query.include("postPtr");
		query.include("photoPtr");
		query.orderByDescending("createdAt");
		return query;
	}

	/*
	 * Functon to Load data into the listview
	 */
	public void loadData() {
		
		ParseQuery<EventActivity> query = fetchEventActivityItems();
			query.findInBackground(new FindCallback<EventActivity>(){
				@Override
				public void done(List<EventActivity> object, ParseException e) {
					eventActivity = object;
					adapter = new EventTimeLnAdapter(getActivity(), new ArrayList<EventActivity>());
					listView.setAdapter(adapter);
					adapter.clear();
					adapter.addAll(eventActivity);
				}
			});
	}
	
	
	public void onCommentPost(View view){
		
		final Posts post = new Posts();
		//Save in the post table
		final ParseUser user = ParseUser.getCurrentUser();
		post.put("userPtr", user);
		final JSONObject eventPtr = new JSONObject();
		
		try {
			eventPtr.put("__type", "Pointer");
			eventPtr.put("className", "Events");
			eventPtr.put("objectId", eventId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post.put("eventPtr", eventPtr);
		EditText postText = (EditText) getView().findViewById(R.id.etPost);
		post.put("post", postText.getText().toString());
		post.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				//create entry in the activity table
		 		EventActivity eActivity = new EventActivity();
		 		String postId = post.getObjectId();
		 		JSONObject postPtr = new JSONObject();
				try {
					postPtr.put("__type", "Pointer");
					postPtr.put("className", "Posts");
					postPtr.put("objectId", postId);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				eActivity.put("postPtr", postPtr);
				eActivity.put("activityFrom", user);
				eActivity.put("eventPtr", eventPtr);
				eActivity.put("eventObj", eventId);		
				eActivity.put("activityType", "post");
			
			eActivity.saveInBackground(new SaveCallback(){
				@Override
				public void done(ParseException e) {
					ParseQuery<EventActivity> query = fetchEventActivityItems();
					query.findInBackground(new FindCallback<EventActivity>(){

						@Override
						public void done(List<EventActivity> objects,
								ParseException e) {
							eventActivity = objects;
							adapter.clear();
							adapter.addAll(eventActivity);
							System.out.println("Added items to adapter: " + eventActivity.size());
							listView.setAdapter(adapter);
							System.out.println("Notifying data set changed.");
							adapter.notifyDataSetChanged();
						}
						
					});

				}
				});
			}
			
		});
		postText.setText("");
	}

}
