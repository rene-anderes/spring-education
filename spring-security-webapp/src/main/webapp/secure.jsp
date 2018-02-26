<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Secure Title</title>
</head>
<body>
	
	<h1>Hello World</h1>
	<p>secure</p>
	<p><a href="#" onclick="javascript:logoutForm.submit();">logout</a></p>
	<c:url var="logoutUrl" value="/logout" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
	    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
	
</body>
</html> 