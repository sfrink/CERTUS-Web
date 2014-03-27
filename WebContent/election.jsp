<%@page import="java.util.ArrayList"%>
<%@page import="dto.ElectionDto"%>
<%@page import="dto.CandidateDto"%>

<%
	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outElections = (String) request.getAttribute("out_elections");
	String outModal = (String) request.getAttribute("out_modal_new");
	String editElection = (String) request.getAttribute("edit_election");
	String existingElections = (String) request.getAttribute("existing_elections");

	
 %>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />
<!-- display a message -->
<%=messageAlert %> 

<div class="row">
	<div class="large-12 columns">

      <div class="row">
		<h3>Elections Management</h3>
		
		
		
		<a href="#" data-reveal-id="out_modal_new" data-reveal
			class="button tiny radius">Add new election</a>
	  </div>

      <div class="row">
	    <%=outElections %>
	</div>
</div>




<%-- <div id="add_new_election" class="reveal-modal" data-reveal>
  <h2>Add new election</h2>
  <%=messageLabel %>
  <%=newElection %>
</div>	
 --%>				

<div id="edit_election" class="reveal-modal" data-reveal>
  <h2>Edit election</h2>
  <%=messageLabel %>
  <%=editElection %>
</div>



<div id="new_election_modal" class="reveal-modal" data-reveal>
	<div class="row">
		<div class="large-6 large-centered medium-6 large-centered columns">
			<%=messageLabel %>
		</div>
	</div>
		
	<%=outModal %>
	<a class="close-reveal-modal">&#215;</a>
</div>

<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#new_election_modal').foundation('reveal', 'open');</script>
<% } %>


<%-- <% if(mode.equals("2")) { %>
  <script>$('#add_new_election').foundation('reveal', 'open');</script>
<% } else if(mode.equals("3")) { %>
  <script>$('#edit_election').foundation('reveal', 'open');</script>	
<% } %>

 --%>
