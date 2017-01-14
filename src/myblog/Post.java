package myblog;


import java.util.Date;

import java.util.*;
 

import com.google.appengine.api.users.User;

import com.googlecode.objectify.annotation.Entity;

import com.googlecode.objectify.annotation.Id; 

@Entity
public class Post implements Comparable<Post>{
    @Id Long id;
    User user;
    String title;
    String content;
    Date date;
    
    private Post(){}
    
    /* Constructor for posts */
    public Post(User u, String text, String tit)
    {
    	this.user = u;
    	this.content = text;
    	this.title = tit;
    	this.date = new Date(); 
    }

    /* Accessor methods */
    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
    
    public String getTitle(){
    	return title;
    }
    
    public Date getDate(){
    	return date;
    }
	@Override
    public int compareTo(Post other) 
	{
        if (date.after(other.date))
        {
            return -1;
        } else if (date.before(other.date)) {
            return 1;
        }else{
        	return 0;
        }
    }
    
}
