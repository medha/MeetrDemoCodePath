package com.hubdub.meetr.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String userName;
	private String lastName;
	private ArrayList<User> facebookFriendList;
	private ArrayList<Event> eventList;
	private ArrayList<EventPhoto> photoList;
	private boolean onMeetr;
	private String facebookUserId;
	
	
	public String getFacebookUserId() {
		return facebookUserId;
	}
	public String getUserName() {
		return userName;
	}
	public String getLastName() {
		return lastName;
	}
	public ArrayList<User> getFacebookFriendList() {
		return facebookFriendList;
	}
	public ArrayList<Event> getEventList() {
		return eventList;
	}
	public ArrayList<EventPhoto> getPhotoList() {
		return photoList;
	}
	public boolean isOnMeetr() {
		return onMeetr;
	}
	
	// Decodes business json into business model object
		public static User fromJson(JSONObject jsonObject) {
			User user = new User();
			// Deserialize json into object fields
			try {
				user.userName = jsonObject.getString("userName");
				user.lastName = jsonObject.getString("lastName");
				user.onMeetr = jsonObject.getBoolean("onMeetr");
				user.facebookUserId = jsonObject.getString("facebookUserId");
			} catch (JSONException exc) {
				exc.printStackTrace();
				return null;
			}
			// Return new object
			return user;
		}
		
		  // Decodes array of business json results into business model objects
		  public static ArrayList<User> fromJson(JSONArray jsonArray) {
		      ArrayList<User> users = new ArrayList<User>(jsonArray.length());
		      // Process each result in json array, decode and convert to business object
		      for (int i=0; i < jsonArray.length(); i++) {
		          JSONObject userJson = null;
		          try {
		          	userJson = jsonArray.getJSONObject(i);
		          } catch (Exception e) {
		              e.printStackTrace();
		              continue;
		          }

		          User user = User.fromJson(userJson);
		          if (user != null) {
		          	users.add(user);
		          }
		      }

		      return users;
		  }


}
