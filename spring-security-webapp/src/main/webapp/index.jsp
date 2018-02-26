<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Startseite</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"> 
</head>
<body class="w3-theme">
	<div class="w3-container">
		<h1>Startseite</h1>
		<p><a href="secure.jsp">secure page</a></p>
		<p><a href="admin">administrator</a></p>
		<p>
			<sec:authorize url="/secure.jsp">
				This content will only be visible to users who are authorized to send requests to the "/secure.jsp" URL.
			</sec:authorize>
		</p>
	</div>
</body>
</html>
