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
//			if(this.selectedUsers == null) {
//				this.selectedUsers.addAll(selectedUsers);
//			} else {
//				for(GraphUser o: selectedUsers) {
//					for(GraphUser oo: this.selectedUsers) {
//						if(!(o.getId().equals(oo.getId()))){
//							this.selectedUsers.add(o);
//						}
//					}
//				}
//			}
	    }
}
