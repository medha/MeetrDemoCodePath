

import java.util.Collection;

import com.facebook.model.GraphUser;

import android.app.Application;

public class Meetr extends Application {
	   private Collection<GraphUser> selectedUsers;

	    public Collection<GraphUser> getSelectedUsers() {
	        return selectedUsers;
	    }

	    public void setSelectedUsers(Collection<GraphUser> selectedUsers) {
	        this.selectedUsers = selectedUsers;
	    }
}
