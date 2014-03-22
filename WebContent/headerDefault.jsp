<%@page import="rmi.Initializer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="resources/js/vendor/jquery.js"></script>
<script src="resources/js/foundation/foundation.js"></script>
<script src="resources/js/vendor/modernizr.js"></script>
<script src="resources/js/foundation/foundation.alert.js"></script>
<script src="resources/js/foundation/foundation.reveal.js"></script>
<script src="resources/js/vendor/jquery.cookie.js"></script>
<script src="resources/js/custom.js"></script>
<link rel="stylesheet" href="resources/css/foundation.css" />


<%
String conStatus = "";
if(request.isSecure()) {
	conStatus = " | secured (https)";
} else {
	conStatus = " | not secured (http)";
}
%>


<title>CERTUS <%=conStatus %></title>
</head>
<body>








  
