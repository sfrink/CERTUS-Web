<%@page import="service.HeaderService"%>
<%
	if(HeaderService.isAuthenticated()) {
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
		rd.forward(request, response);
	}

	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outModal = (String) request.getAttribute("out_modal");
%>

<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" />

<%=messageAlert %>
  
  <div class="row">
	<h3>Welcome to Certus Voting System!</h3> 
	<h5>This wizard  will help you to setup a new user account.</h5>
	
	<form action="signup" method="post">	
	  <button class="button radius" type="submit" name="button_add_user" value="new">Yes, let's start</button>		
	</form>
	
	<div id="modal_window" class="reveal-modal" data-reveal>
		<div class="row">
			<%=messageLabel %>
		</div>
		
		<%=outModal %>
		<a id="close-reveal-modal-modified" class="close-reveal-modal">&#215;</a>
	</div>
  </div>

<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#modal_window').foundation('reveal', 'open');</script>
<% } %>
  