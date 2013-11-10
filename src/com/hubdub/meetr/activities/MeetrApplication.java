package com.hubdub.meetr.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.parse.Parse;

public class MeetrApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "rcJ9OjhbQUqRqos6EusNdnwGEYNC9d4a6rXdqAMU",
				"3SRkJuZREKUG3bwvMsjYXOsPXqSdzONx6MzaXWAH");
	}
	private Collection<GraphUser> selectedUsers = new ArrayList<GraphUser>();
	private GraphPlace selectedPlace;
//	File cacheDir = StorageUtils.getCacheDirectory(this);
//	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//		.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
//        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//	    .memoryCacheSize(2 * 1024 * 1024)
////	    .discCache(new UnlimitedDiscCache(cacheDir)) // default
//	    .discCacheSize(50 * 1024 * 1024)
//	    .discCacheFileCount(100)
//	    .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//	    .build();
	

	public Collection<GraphUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(Collection<GraphUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public void setSelectedPlace(GraphPlace selectedPlace) {
		this.selectedPlace = selectedPlace;
	}
	
	public GraphPlace getSelectedPlace() {
		return selectedPlace;
	}
	
	

}
