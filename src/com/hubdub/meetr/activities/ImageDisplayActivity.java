package com.hubdub.meetr.activities;

import com.hubdub.meetr.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		//ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
		ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
		//ivImage.setImageUrl(imageResult.getFullUrl());
	}
}
