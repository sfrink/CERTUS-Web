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
      <div class="row"><a href="election" class="button radius">Add / Edit Elections</a></div>
	<% } %>

	<% if(HeaderService.hasAccess("voting")) { %>
	  <div class="row"><a href="voting" class="button radius">Vote</a></div>
	<% } %>
	  
	<% if(HeaderService.hasAccess("results")) { %>
	  <div class="row"><a href="results" class="button radius">Elections Results</a></div>
	<% } %>
	
	<% if(HeaderService.hasAccess("adminElection")) { %>
	  <div class="row"><a href="adminElection" class="button radius">Delete Elections</a></div>
	<% } %>

	<% if(HeaderService.hasAccess("adminUser")) { %>
      <div class="row"><a href="adminUser" class="button radius">Edit Users</a></div>
	<% } %>

	<% if(HeaderService.hasAccess("newkey")) { %>
	  <div class="row"><a href="newkey" class="button radius">Key Management</a></div>
	<% } %>

	<% if(HeaderService.hasAccess("profile")) { %>
	  <div class="row"><a href="profile" class="button radius">Edit Profile</a></div>
	<% } %>
  </div>
</div>

<jsp:include page="footer.jsp" />