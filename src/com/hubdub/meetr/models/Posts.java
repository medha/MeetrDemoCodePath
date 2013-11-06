package com.hubdub.meetr.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Posts")
public class Posts extends ParseObject {

	public Posts() {
		
	}
	public String getPost() {
		return getString("post");
	}

}
