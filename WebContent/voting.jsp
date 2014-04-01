<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%
String mode = (String) request.getAttribute("mode");	
String messageAlert = (String) request.getAttribute("message_alert");
String messageLabel = (String) request.getAttribute("message_label");
String outElections = (String) request.getAttribute("out_elections");
String outModal = (String) request.getAttribute("out_modal");
%>

<%=messageAlert %>

<div class="row">
	<div class="large-12 columns">
    	<h3>Voting</h3>

		<%=outElections %>	
	</div>	

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


