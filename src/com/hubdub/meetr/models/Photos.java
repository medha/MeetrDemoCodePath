package com.hubdub.meetr.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Photos")
public class Photos extends ParseObject{

	public Photos() {
		
	}
	
	public ParseFile getPhoto() {
		return getParseFile("photo");
	}
	
}
