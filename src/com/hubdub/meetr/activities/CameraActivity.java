package com.hubdub.meetr.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.hubdub.meetr.R;

public class CameraActivity extends Activity {

	/*
	 * Learned this from Stack Overflow Example 
	 * Link src: http://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
	 */
	private static final int CAMERA_REQUEST = 1888; 
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
	    this.imageView = (ImageView)this.findViewById(R.id.imageView1);
		getActionBar().setDisplayHomeAsUpEnabled(true);

   	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
        }  
    } 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			finish();
			return true;
		case R.id.action_trash:
			finish();
			return true;
		case android.R.id.home:
			onBackPressed();  //This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
