package com.hubdub.meetr.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.hubdub.meetr.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PlacesActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		AsyncHttpClient client = new AsyncHttpClient();
		Log.d("DEBUG", "test");
		client.get("http://www.google.com", new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.d("DEBUG",response.toString());
		    }
		});
	}
}
