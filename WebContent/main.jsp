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

	<% if(HeaderService.hasAccess("election")) { %>
      <p><a href="election" class="button radius">Add / Edit Elections</a></p>
	<% } %>

	<% if(HeaderService.hasAccess("voting")) { %>
	  <p><a href="voting" class="button radius">Vote</a></p>
	<% } %>
	  
	<% if(HeaderService.hasAccess("results")) { %>
	  <p><a href="results" class="button radius">Elections Results</a></p>
	<% } %>
	
	<% if(HeaderService.hasAccess("adminElection")) { %>
	  <p><a href="adminElection" class="button radius">Delete Elections</a></p>
	<% } %>

	<% if(HeaderService.hasAccess("adminUser")) { %>
      <p><a href="adminUser" class="button radius">Edit Users</a></p>
	<% } %>

  </div>
</div>

<jsp:include page="footer.jsp" />