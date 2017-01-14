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


public class Posting_Servlet extends HttpServlet {
	/* Register all objectified stuff */
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp)throws IOException {
        /* Attempt to get the current visitor to sign in */
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user != null) 
        {
        	resp.sendRedirect("posting.jsp");
        } else 
        {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
            resp.sendRedirect("posting.jsp");
        }
    }
}
