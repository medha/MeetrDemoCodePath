package com.hubdub.meetr.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Events")
public class Events extends ParseObject {

	public Events() {
	}

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

}
