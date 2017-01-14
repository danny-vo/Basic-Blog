<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="java.util.List"%>

<%@ page import="com.google.appengine.api.users.User"%>

<%@ page import="com.google.appengine.api.users.UserService"%>

<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<%@ page
	import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>

<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>

<%@ page import="com.google.appengine.api.datastore.Query"%>

<%@ page import="com.google.appengine.api.datastore.Entity"%>

<%@ page import="com.google.appengine.api.datastore.FetchOptions"%>

<%@ page import="com.google.appengine.api.datastore.Key"%>

<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="myblog.Post"%>

<%@ page import="com.googlecode.objectify.*"%>

<%@ page import="java.util.*"%>
<%@ page import="myblog.BlogUser"%>

<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<head>
<style>
.center {
    margin: auto;
    width: 30%;
    border: 3px solid #00FFFF;
    padding: 10px;
}
</style>
</head>
<body>
	<%
		String blogName = request.getParameter("blogName");

		if (blogName == null) {

			blogName = "Danny's Blog";

		} else {
		}

		pageContext.setAttribute("blogName", "Danny's Blog");

		UserService userService = UserServiceFactory.getUserService();

		User user = userService.getCurrentUser();

		if (user != null) {
			//BlogUser currentUser = null;
			if (user != null) {
				pageContext.setAttribute("user", user);
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				ObjectifyService.register(BlogUser.class);
				List<BlogUser> users = ObjectifyService.ofy().load().type(BlogUser.class).list();
				boolean userRegistered = false;
				for (BlogUser bu : users) {
					if (user.getEmail().equals(bu.getEmail())) {
						userRegistered = true;
						//currentUser = bu;
						break;
					}
				}
				if (!userRegistered) {
					BlogUser newUser = new BlogUser(user.getEmail());
					ObjectifyService.ofy().save().entity(newUser).now();
				}
			}
			pageContext.setAttribute("user", user);
	%>



	<p>
		Hello, ${fn:escapeXml(user.nickname)}! You can <a
			href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
			out</a> or return to the
		<td><a href="login.jsp"> User Menu!</a></td>
	</p>


	<%
		} else {
	%>
	<p>
		Hello! <a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
			in</a> to being posting on the blog!
	</p>
	<%
		}
	%>
	<%
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key blogKey = KeyFactory.createKey("Blog", blogName);
		// Run an ancestor query to ensure we see the most up-to-date

		// view of the Greetings belonging to the selected Guestbook.

		//Query query = new Query("Greeting", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);

		//List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		ObjectifyService.register(Post.class);
		List<Post> posts = ObjectifyService.ofy().load().type(Post.class).list();
		Collections.sort(posts);
		if (posts.isEmpty()) {
	%>
	<p>'${fn:escapeXml(blogName)}' has no messages.</p>

	<%
		} else {
	%>
	<center>
	<p>Posts in '${fn:escapeXml(blogName)}':</p></center>
	<%
		int cap = 5;
			for (Post post : posts) {
				pageContext.setAttribute("post_title", post.getTitle());
				pageContext.setAttribute("post_content", post.getContent());
				if (post.getUser() == null) {
	%>
	<p>An anonymous person posted:</p>
	<%
		} else {
					pageContext.setAttribute("post_user", post.getUser());
	%>
	<center>
	<p>
		<b>${fn:escapeXml(post_user.nickname)}</b> posted:
	</p></center>
	<%
		}
	%>
	<div class="center">
	<center><h2>${fn:escapeXml(post_title)}</h2></center>
	<blockquote>${fn:escapeXml(post_content)}</blockquote></div>
	<%
			}
		}
	%>
	<%
		if (user != null) {
	%>
	<form action="/posting" method="post">
		<div>
			<input type="submit" value="Post Blog Entry" />
		</div>

		<input type="hidden" name="guestbookName"
			value="${fn:escapeXml(blogName)}" />
	</form>
	<%
		}
	%>

	<form action="/blog" method="post">
		<div>
			<input type="submit" value="Show only recent posts" />
		</div>
	</form>
	<iframe width="0" height="0" border="0" name="dummyframe"
		id="dummyframe"></iframe>
</body>
</html>