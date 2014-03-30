<%@page import="service.HeaderService"%>
<%
if(!HeaderService.isAuthenticated()) {
 	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);		
}
%>

<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%
String messageAlert = (String) request.getAttribute("message_alert");
%>

<%=messageAlert %>


<div class="row">
  <div class="large-12 columns">

	<h3>Welcome Screen | Authorized User</h3>

	<div class="row">
	  <a href="election" class="button radius">Add / Edit Elections</a>
	</div>
	
	<div class="row">  
		<a href="voting" class="button radius">Vote</a>
	</div>
	
	<div class="row">
		<a href="results" class="button radius">Elections Results</a>
	</div>
	
	<div class="row">
		<a href="adminElection" class="button radius">Delete Elections</a>
	</div>
	
  </div>
</div>

<jsp:include page="footer.jsp" />