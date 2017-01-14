package myblog;

import java.util.Date;

import java.util.*;

import com.google.appengine.api.users.User;

import com.googlecode.objectify.annotation.Entity;

import com.googlecode.objectify.annotation.Id;

@Entity
public class BlogUser implements Comparable<BlogUser> {
	@Id
	Long id;
	Boolean subscribed = true;
	String userEmail;

	private BlogUser() {

	}

	public BlogUser(String email) {
		userEmail = email;
	}
	
	public BlogUser(String email, boolean x){
		subscribed = x;
		userEmail = email;
		
	}

	/* Accessor methods */
	public String getEmail() {
		return userEmail;
	}

	public Boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscription() {
		if (this.subscribed == true) {
			this.subscribed = false;
		} else {
			this.subscribed = true;
		}
	}
	
	public void activateSub(){
		this.subscribed = true;
	}
	
	public void deactivateSub(){
		this.subscribed = false;
	}

	@Override
	public int compareTo(BlogUser o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
