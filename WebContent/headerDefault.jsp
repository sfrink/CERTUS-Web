<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="platform.dto.HelloDto" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="http://foundation.zurb.com/assets/css/templates/foundation.css">
<script src="http://foundation.zurb.com/assets/js/modernizr.js"></script>
<script src="http://foundation.zurb.com/assets/js/jquery.js"></script>
<script src="http://foundation.zurb.com/assets/js/templates/foundation.js"></script>

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


   
<!-- Header and Nav -->
  
  <div class="row">
    <div class="large-3 columns">
      <h1><img src="http://placehold.it/400x100&text=Logo" /></h1>
    </div>
    <div class="large-9 columns">
      <ul class="inline-list right">
        <li><a href="#">Section 1</a></li>
        <li><a href="#">Section 2</a></li>
        <li><a href="#">Section 3</a></li>
        <li><a href="#">Section 4</a></li>
      </ul>
    </div>
  </div>
  
  <!-- End Header and Nav -->
  
