<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outUsers = (String) request.getAttribute("out_users");
	String outModal = (String) request.getAttribute("out_modal");
 %>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />
<!-- display a message -->
<%=messageAlert %> 

<div class="row">
	<div class="large-12 columns">

      <div class="row">
		<h3>User Management</h3>
	  </div>

      <div class="row">
	    <%=outUsers %>
	</div>
</div>


<div id="election_modal" class="reveal-modal" data-reveal>
	<div class="row"><%=messageLabel %></div>
		
	<%=outModal %>
	<a class="close-reveal-modal">&#215;</a>
</div>

<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#user_modal').foundation('reveal', 'open');</script>
<% } %>