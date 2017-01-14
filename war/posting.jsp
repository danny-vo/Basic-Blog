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

<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>

<html>
<body>
	<%
		String blogName = request.getParameter("blogName");

		if (blogName == null) {

			blogName = "Danny's Blog";

		} else {
		}

		pageContext.setAttribute("blogName", "Danny's Blog");
	%>
	<center>
		<form action="/blogsign" method="post">
			<h1>Create a blog post!</h1>
			<h4>Post title:</h4>
			<div>
				<textarea name="title" rows="1" cols="60"></textarea>
			</div>

			<h4>Post content:</h4>
			<div>
				<textarea name="content" rows="6" cols="60"></textarea>
			</div>

			<div>
				<input type="submit" value="Post Blog Entry" />
			</div>

			<input type="hidden" name="guestbookName"
				value="${fn:escapeXml(blogName)}" />

		</form>

		<form action="/blog" method="post">
			<div>
				<input type="submit" value="Cancel post" />
			</div>
		</form>
	</center>
</body>
</html>