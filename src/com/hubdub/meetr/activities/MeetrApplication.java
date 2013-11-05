package com.hubdub.meetr.activities;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Application;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
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
