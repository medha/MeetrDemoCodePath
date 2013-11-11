package com.hubdub.meetr.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Suggestions")
public class Suggestions extends ParseObject {


	public String getEventName() {
		return getString("EventName");
	}

	public void setEventName(String eventName) {
		put("EventName", eventName);
	}

	public void setEventDate(Date eventDate) {
		put("EventDate", eventDate);
	}

	public Date getEventDate() {
		return getDate("EventDate");
	}

	public void setEventTime(Date eventTime) {
		put("EventTime", eventTime);
	}

	public Date getEventTime() {
		return getDate("EventTime");
	}

	public void setCurrentUser(ParseUser currentUser) {
		put("User", currentUser);
	}

	public String getRsvp() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		return getString("Description");
	}

	public Bitmap getEventImageUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLocation() {
		return getString("Location");
	}
	
	public void setLocation(String name) {
		put("Location", name);
	}
	
	public JSONArray getGuestList() {
		return getJSONArray("GuestList");
	}
	
	public void setGuestList(JSONArray guestList) {
		put("GuestList", guestList);
	}

	public void setEventDescription(String description) {
		put("Description", description);
	}

	public String getEventId() {
		return getString("objectId");
		
	}
	
	public void setFbGuestList(ArrayList<String> fbGuestList) {
		put("FbGuestList", fbGuestList);
	}
	
	public ArrayList<String> getFbGuestList() {
		return getFbGuestList();
	}
}
