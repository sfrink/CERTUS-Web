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
    	<h3>Voting Results</h3>

		<%=outElections %>	
	</div>	

	<div id="voting_modal" class="reveal-modal" data-reveal>
		<div class="row">
			<div class="large-6 large-centered medium-6 large-centered columns">
				<%=messageLabel %>
			</div>
		</div>
		
		<%=outModal %>
		<a class="close-reveal-modal">&#215;</a>
	</div>
</div>


<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#voting_modal').foundation('reveal', 'open');</script>
<% } %>

