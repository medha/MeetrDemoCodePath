package com.hubdub.meetr.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.hubdub.meetr.R;

public class ComposeAcitivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		/*
		 * Retireive data sent by the Parent activity. This could be useful in
		 * sending the API keys among other things.
		 * TODO Get rid of this
		 */
		String dummyApiKey = getIntent().getStringExtra("dummyApiKey");

		/*
		 * Testing toast out here
		 * TODO Get rid of this
		 */
		Toast toast = Toast.makeText(getApplicationContext(),
				"Create a new Event with Api Key=" + dummyApiKey,
				Toast.LENGTH_SHORT);
		
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();

	}
}
