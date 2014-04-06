<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" /> 


<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outModal = (String) request.getAttribute("out_modal");
 %>


<%=messageAlert %>
  
<div class="row">
	<h3>Welcome to Certus Voting System!</h3> 
	<h5>
		In this wizard we will create a new account for you, and will generate a private key 
		especially  for you so you can sign your votes (don't worry it's not that complicated),
		so should we start?
	</h5>
	<div class="row">
		<form action="signup" method="post">	
			<button class="button radius" type="submit" name="button_add_user" value="new">Yes, let's start</button>		
		</form>
		<a href="login">No, just forget about it.</a>
	</div>
	
	<div id="modal_window" class="reveal-modal" data-reveal>
		<div class="row">
			<%=messageLabel %>
		</div>
		
		<%=outModal %>
	<a id="close-reveal-modal-modified_election" class="close-reveal-modal">&#215;</a>
	</div>
</div>



<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#modal_window').foundation('reveal', 'open');</script>
<% } %>
  