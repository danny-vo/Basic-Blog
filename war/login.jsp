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

<%@ page import="myblog.BlogUser"%>

<%@ page import="com.googlecode.objectify.*"%>

<%@ page import="java.util.*"%>

<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
	<%
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		BlogUser currentUser = null;
		if (user != null) {
			pageContext.setAttribute("user", user);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			ObjectifyService.register(BlogUser.class);
			List<BlogUser> users = ObjectifyService.ofy().load().type(BlogUser.class).list();
			boolean userRegistered = false;
			for (BlogUser bu : users) {
				if (user.getEmail().equals(bu.getEmail())) {
					userRegistered = true;
					currentUser = bu;
					break;
				}
			}
			if (!userRegistered) {
				BlogUser newUser = new BlogUser(user.getEmail());
				ObjectifyService.ofy().save().entity(newUser).now();
			}
	%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>User Menu</title>
</head>

<head>
<style>
.right {
	position: absolute;
	right: 0px;
	width: 680px;
	padding: 10px;
}
</style>
</head>

<body>
	<h1>
		<p>
			Hello, ${fn:escapeXml(user.nickname)}! (You can <a
				href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
				out</a>.)
		</p>
	</h1>
	<%
		if (currentUser.isSubscribed()) {
	%>
	<p>You are subscribed to email updates!</p>
	<%
		} else {
	%>
	<p>You are not subscribed to email updates!</p>
	<%
		}
	%>

	<div class="right"><body>

	<img src="flower2.jpg" alt="flower_rose"
		style="width: 608px; height: 456px;">

</body>
</div>
	<table>
		<tr>
			<td colspan="2" style="font-weight: bold;">Navigation Options:</td>
		</tr>
		<tr>
			<td><a href="danny_vo_blog">Main Blog</a></td>
		</tr>
		<%
			if (currentUser.isSubscribed()) {
		%>
		<form action="/email_settings" method="post">
			<div>
				<input type="submit" value="Unsubscribe from emails" />
			</div>

		</form>
		<%
			} else {
		%>
		<form action="/email_settings" method="post">
			<div>
				<input type="submit" value="Subscribe to emails" />
			</div>

		</form>
		<%
			}
		%>
	</table>

</body>


</html>


<%
	} else {
%>
<p>
	Hello! <a
		href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
		in</a> to being posting on the blog!
</p>
<p>
	Or skip logging in and
	<tr>
		<td><a href="blog.jsp">view blog without posting</a></td>
	</tr>
</p>


<%
	}
%>


</body>
</html>