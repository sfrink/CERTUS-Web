<%@page import="service.HeaderService"%>
<%
if(!HeaderService.isAuthenticated(request) ||
   !HeaderService.hasAccess(request, "adminElection")) {
	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);
}
%>

<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outElections = (String) request.getAttribute("out_elections");
	String outModal = (String) request.getAttribute("out_modal");
 %>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />
<!-- display a message -->
<%=messageAlert %> 

<div class="row">
	<div class="large-12 columns">

      <div class="row">
		<h3>Election Management</h3>
	  </div>

      <div class="row">
	    <%=outElections %>
	</div>
</div>


<div id="modal_window" class="reveal-modal" data-reveal>
	<div class="row"><%=messageLabel %></div>
		
	<%=outModal %>
	<a id="close-reveal-modal-modified-force" class="close-reveal-modal">&#215;</a>
</div>

<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#modal_window').foundation('reveal', 'open');</script>
<% } %>
