package myblog;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.googlecode.objectify.ObjectifyService.ofy;


public class Blog_Servlet extends HttpServlet {
	/* Register all objectified stuff */
	static {ObjectifyService.register(Post.class);}
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp)throws IOException {
    	//Grab user data and inputs to create their post 
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String content = req.getParameter("content");
        String title = req.getParameter("title");
        Date date = new Date();
        Post post= new Post(user, content, title);
        
        //Save the newly created post into our post history  
        ObjectifyService.ofy().save().entity(post).now();
        String blogName = req.getParameter("BlogName");
        resp.sendRedirect("blog.jsp");
    }
}
