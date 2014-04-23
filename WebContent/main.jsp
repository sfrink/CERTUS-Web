<%@page import="service.HtmlService"%>
<%@page import="service.HeaderService"%>
<%
if(!HeaderService.isAuthenticated()) {
 	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);		
}

String mode = (String) request.getAttribute("mode");
String messageAlert = (String) request.getAttribute("message_alert");
String messageLabel = (String) request.getAttribute("message_label");
String outElectionsForVoting = (String) request.getAttribute("out_elections_for_voting");
String outElectionsForResults = (String) request.getAttribute("out_elections_for_results");
String outModal = (String) request.getAttribute("out_modal");
%>

<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%=messageAlert %>

<div class="row" data-equalizer>
	<% if(HeaderService.hasAccess("election")) { %>
  	  <div class="large-12 columns">
        <div class="left">
          <a href="election" class="button tiny radius">Manage my elections</a>
          <%= HtmlService.getToolTip("You can start your own election or manage existing elections.") %>
        </div>
 	  </div>
	<% } %>

  <div class="large-12 columns">
    <div class="large-6 medium-6 small-6 columns panel clear" data-equalizer-watch>
	  <h5>You can vote in the following elections:</h5>
	  <%=outElectionsForVoting %>
    </div>

    <div class="large-6 medium-6 small-6 columns panel clear" data-equalizer-watch>
	  <h5>Elections with published results:</h5>	  
	  <%=outElectionsForResults %>
    </div>
  </div>
</div>


<!-- MODAL -->
<div id=modal_window class="reveal-modal" data-reveal>
  <div class="row">
    <div class="large-6 large-centered medium-6 large-centered columns">
	  <%=messageLabel %>
	</div>
  </div>
  <%=outModal %>
</div>
<!-- MODAL END -->



<div class="row">
  <div class="large-12 columns">
	<% if(HeaderService.hasAccess("adminElection")) { %>
	  <p><a href="adminElection" class="button radius">Delete Elections</a></p>
	<% } %>

	<% if(HeaderService.hasAccess("adminUser")) { %>
      <p><a href="adminUser" class="button radius">Edit Users</a></p>
	<% } %>
  </div>
</div>

<jsp:include page="footer.jsp" />

<% if(mode.equals("2")) { %>
  <script>$('#modal_window').foundation('reveal', 'open');</script>
<% } %>
