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
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class EmailSettings_Servlet extends HttpServlet {
	static {
		ObjectifyService.register(BlogUser.class);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		BlogUser currentUser = null;
		if (user != null) {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			ObjectifyService.register(BlogUser.class);
			List<BlogUser> users = ObjectifyService.ofy().load().type(BlogUser.class).list();
			boolean userRegistered = false;
			for (BlogUser bu : users) {
				if (user.getEmail().equals(bu.getEmail())) {
					boolean isSubscribed = bu.isSubscribed();
					ofy().delete().type(BlogUser.class).id(bu.id);
					BlogUser temp = new BlogUser(user.getEmail(), !isSubscribed);
					ObjectifyService.ofy().save().entity(temp).now();
					break;
				}
			}
			//currentUser.setSubscription();
			resp.sendRedirect("login.jsp");
		}else{ resp.sendRedirect("blog.jsp"); }
	}

}
