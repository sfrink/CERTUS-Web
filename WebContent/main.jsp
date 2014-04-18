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
        <div class="right">
          <a href="election" class="button tiny radius">Manage my elections</a>&nbsp
          <span data-tooltip class="has-tip tip-top tip-right" title="You can start your own election or manage elections you have been working on.">(?)</span>
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


<div class="row">
  <div class="large-12 columns">


	<% if(HeaderService.hasAccess("voting")) { %>
	  <p><a href="voting" class="button radius">Vote</a></p>
	<% } %>
	  
	<% if(HeaderService.hasAccess("results")) { %>
	  <p><a href="results" class="button radius">Elections Results</a></p>
	<% } %>
	
	<% if(HeaderService.hasAccess("adminElection")) { %>
	  <p><a href="adminElection" class="button radius">Delete Elections</a></p>
	<% } %>

	<% if(HeaderService.hasAccess("adminUser")) { %>
      <p><a href="adminUser" class="button radius">Edit Users</a></p>
	<% } %>

  </div>
</div>

<jsp:include page="footer.jsp" />