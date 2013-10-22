package com.hubdub.meetr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class EventPhoto {

	private String photoId;
	private String url;

	public String getPhotoId() {
		return photoId;
	}
	public String getUrl() {
		return url;
	}
	
	// Decodes business json into business model object
		public static EventPhoto fromJson(JSONObject jsonObject) {
			EventPhoto photo = new EventPhoto();
			// Deserialize json into object fields
			try {
				photo.photoId = jsonObject.getString("photoId");
				photo.url = jsonObject.getString("url");
			} catch (JSONException exc) {
				exc.printStackTrace();
				return null;
			}
			// Return new object
			return photo;
		}
		
	
}
