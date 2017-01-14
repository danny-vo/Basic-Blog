package myblog;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Compatible_MatchesServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect("blogunsigned.jsp");
	}
}
