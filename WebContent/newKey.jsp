<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outModal = (String) request.getAttribute("out_modal");
 %>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />
<!-- display a message -->
<%=messageAlert %> 

<div class="row">
	<h3>Generate New Signing Key</h3> 
	<h5>
		Warning: If you generate a new signing key, All your voting in any un-closed elections will not be casted, 
		and you will not be able to re-vote again in those un-closed elections.
	</h5>
	<h5>
	<font color="#FF0000">
		Are you sure you want to generate new signing key?
	</font>
	</h5>
	<div class="row">
		<form action="newkey" method="post">	
			<button class="button radius" type="submit" name="button_start" value="new">Yes, let's do it</button>		
		</form>
		<a href="login">No, get me out of here</a>
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