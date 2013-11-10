package com.hubdub.meetr.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Posts")
public class Posts extends ParseObject {

	public Posts() {
		
	}
	public String getPost() {
		return getString("post");
	}

}
