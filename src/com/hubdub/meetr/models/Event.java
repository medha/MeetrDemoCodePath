package com.hubdub.meetr.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	private String eventId;
	private String name;
	private String description;
	private String date;
	private String time;
	private String eventLocation;
	private User eventCreator;
	private ArrayList<User> eventInvitees;
	private ArrayList<EventPhoto> eventPhotos;

	public String getVenueAddress() {
		return eventLocation;
	}

	public String getTime() {
		return time;
	}

	public String getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}
	

	public User getEventCreator() {
		return eventCreator;
	}

	public ArrayList<User> getEventInvitees() {
		return eventInvitees;
	}


	public ArrayList<EventPhoto> getEventPhotos() {
		return eventPhotos;
	}

	// Decodes business json into business model object
	public static Event fromJson(JSONObject jsonObject) {
		Event e = new Event();
		// Deserialize json into object fields
		try {
			e.eventId = jsonObject.getString("eventId");
			e.name = jsonObject.getString("name");
			e.description = jsonObject.getString("description");
			e.date = jsonObject.getString("date");
			e.time = jsonObject.getString("time");
			e.eventLocation = jsonObject.getString("eventLocation");
			e.eventInvitees = User.fromJson(jsonObject.getJSONArray("eventInvitees"));
		} catch (JSONException exc) {
			exc.printStackTrace();
			return null;
		}
		// Return new object
		return e;
	}
	
	  // Decodes array of business json results into business model objects
	  public static ArrayList<Event> fromJson(JSONArray jsonArray) {
	      ArrayList<Event> events = new ArrayList<Event>(jsonArray.length());
	      // Process each result in json array, decode and convert to business object
	      for (int i=0; i < jsonArray.length(); i++) {
	          JSONObject businessJson = null;
	          try {
	          	businessJson = jsonArray.getJSONObject(i);
	          } catch (Exception e) {
	              e.printStackTrace();
	              continue;
	          }

	          Event event = Event.fromJson(businessJson);
	          if (event != null) {
	          	events.add(event);
	          }
	      }

	      return events;
	  }


}
