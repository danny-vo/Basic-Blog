package myblog;

//[START simple_includes]
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//[END simple_includes]

//[START multipart_includes]
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
//[END multipart_includes]
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;

import com.googlecode.objectify.annotation.Id;

@SuppressWarnings("serial")
public class CronTask_Servlet extends HttpServlet {
	static {
		ObjectifyService.register(BlogUser.class);
	}
	static {
		ObjectifyService.register(Post.class);
	}
	static final long DAY = 24 * 60 * 60 * 1000;
	private static final Logger _logger = Logger.getLogger(CronTask_Servlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		_logger.info("Cron Job has started");
		/* Assemble the send string */
		String emailContent = "";
		
		int ctr = 0;
		List<Post> posts = ObjectifyService.ofy().load().type(Post.class).list();
		Collections.sort(posts);
		for (Post post : posts) {
			if (inLast24(post.getDate())) {
				ctr += 1;
				emailContent += post.getUser().getNickname() + " posted:\n";
				emailContent += post.getTitle() + "\n\n";
				emailContent += post.getContent() + "\n\n";
			}
			_logger.info("Cron Job has gone through posts");
		}



		try {
			/* There is stuff to be sent */
			if(ctr > 0){
				
				// Assuming you are sending email from localhost
				emailContent += "Here are the blog posts made in the past 24 hours!\n\n";
				String host = "localhost";

				// Get system properties
				Properties properties = System.getProperties();

				// Setup mail server
				properties.setProperty("mail.smtp.host", host);

				// Get the default Session object.
				Session session = Session.getDefaultInstance(properties);
				List<BlogUser> users = ObjectifyService.ofy().load().type(BlogUser.class).list();
				for (BlogUser bu : users) {
					if (bu.isSubscribed()) {
						MimeMessage message = new MimeMessage(session);
						// Set From: header field of the header.
						message.setFrom(new InternetAddress("danny.vo@utexas.edu"));
		
						// Set To: header field of the header.
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(bu.getEmail()));
		
						// Set Subject: header field
						message.setSubject("Danny's Blog Recent Posts");
		
						// Now set the actual message
						message.setText(emailContent);
		
						// Send message
						Transport.send(message);
						_logger.info("Cron Job has executed");
					}
	
				}
			}

		} catch (Exception ex) {
			_logger.info("Cron Job has been fucked up");
		}
		// }

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public boolean inLast24(Date aDate) {
		return aDate.getTime() > System.currentTimeMillis() - DAY;
	}
}