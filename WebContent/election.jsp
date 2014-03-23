<%@page import="java.util.ArrayList"%>
<%@page import="dto.ElectionDto"%>
<%@page import="dto.CandidateDto"%>

<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String existingElections = (String) request.getAttribute("existing_elections");
	String newElection = (String) request.getAttribute("new_election");
	String editElection = (String) request.getAttribute("edit_election");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
 %>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />
<!-- display a message -->
<%=messageAlert %> 

<div class="row">
	<div class="large-12 columns">

      <div class="row">
		<h3>Elections Management</h3>
		<a href="#" data-reveal-id="add_new_election" data-reveal
			class="button tiny radius">Add new election</a>
	  </div>

      <div class="row">
	    <%=existingElections %>
	</div>
</div>




<div id="add_new_election" class="reveal-modal" data-reveal>
  <h2>Add new election</h2>
  <%=messageLabel %>
  <%=newElection %>
</div>	
				

<div id="edit_election" class="reveal-modal" data-reveal>
  <h2>Edit election</h2>
  <%=messageLabel %>
  <%=editElection %>
</div>









				

		
















<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#add_new_election').foundation('reveal', 'open');</script>
<% } else if(mode.equals("3")) { %>
  <script>$('#edit_election').foundation('reveal', 'open');</script>	
<% } %>

