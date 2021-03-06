package com.hubdub.meetr.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.hubdub.meetr.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
        final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		
		 // get intent data
        Intent i = getIntent();
 
        // Selected image id
        String url = (String) i.getStringExtra("url");
 
		ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		imageLoader.displayImage(url, ivImage);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();  //This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	     
}
