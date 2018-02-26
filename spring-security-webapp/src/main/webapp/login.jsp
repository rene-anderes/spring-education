<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Logon</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"> 
</head>
<body class="w3-theme">
	<div class="w3-container">
		<h1>Login</h1>
		<c:url value="/login.jsp" var="loginUrl" />
		<form action="${loginUrl}" method="post">
			<c:if test="${param.error != null}">
				<p>Invalid username and password.</p>
			</c:if>
			<c:if test="${param.logout != null}">
				<p>You have been logged out.</p>
			</c:if>
			<input type="text" class="w3-input" id="username" name="username" />
			<label for="username">Username</label> 
 			<input type="password" class="w3-input" id="password" name="password" />
			<label for="password">Password</label>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<p><button class="w3-button w3-red" type="submit" class="btn">Log in</button></p>
		</form>
		<p><a href="index.jsp">zurück zur Startseite</a></p>
	</div>
</body>
</html>