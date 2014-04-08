<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outModal = (String) request.getAttribute("out_modal");
	String outFile = (String) request.getAttribute("out_file");
 %>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />
<!-- display a message -->
<%=messageAlert %> 

<div class="row">

	<h3>Manage Your Keys</h3>
	<%=outFile %>
		
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