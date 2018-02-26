<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Administrator Startseite</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"> 
</head>
<body class="w3-theme">
	<div class="w3-container">
		<h1>Administrator Startseite</h1>
		<p>Diese Seite wird nur angezeigt, wenn sich der Benutzer mit der Rolle "ADMIN" authentisiert hat.</p>
		<p><a href="index.jsp">zurück zur Startseite</a></p>
		<p><a href="#" onclick="javascript:logoutForm.submit();">logout</a>
		<c:url var="logoutUrl" value="/logout" />
		<form action="${logoutUrl}" method="post" id="logoutForm">
		    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</div>
</body>
</html>