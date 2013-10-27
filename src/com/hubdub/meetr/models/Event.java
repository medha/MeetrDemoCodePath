package com.hubdub.meetr.models;


public class Event {
	private static int eventCount;
	public String eventName;
	public String eventDate;
	public String eventTime;
	public Event(){
		eventCount++;
		this.eventName = "EventTitle"+eventCount;
		this.eventDate = Integer.toString(eventCount);
		this.eventTime = Integer.toString(eventCount);
	}
	/* Get Event Name */
	public String getEventName(){
		return eventName;
	}
	/* Get Event Date */
	public String getEventDate(){
		return eventDate;
	}
	public String getEventTime(){
		return eventTime;
	}
	public void resetEventCount(){
		eventCount=0;
	}
}

