package com.hubdub.meetr.activities;


import java.util.ArrayList;
import java.util.Collection;

import android.app.Application;

import com.facebook.model.GraphUser;

public class MeetrApplication extends Application {
	   private Collection<GraphUser> selectedUsers = new ArrayList<GraphUser>();

	   public Collection<GraphUser> getSelectedUsers() {
	        return selectedUsers;
	    }
	    public void setSelectedUsers(Collection<GraphUser> selectedUsers) {
	    	this.selectedUsers = selectedUsers;
	    }
}
