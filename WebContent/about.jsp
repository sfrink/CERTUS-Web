<%@page import="service.HeaderService"%>

<jsp:include page="headerDefault.jsp" />

<% if(HeaderService.isAuthenticated(request)) { %>
  <jsp:include page="headerTopBarAuthenticated.jsp" /> 	
<% } else { %>
  <jsp:include page="headerTopBarDefault.jsp" />
<% } %>

<div class="row">
  <div class="large-12 medium-12 small-12 columns">
	<h4>About CERTUS</h4>
	<div class="global_body_text">
	  CERTUS is a secure voting system developed at George Washington University. 
	  It currently utilizes a simple double envelope voting protocol that provides some basic privacy, integrity, and verifiability properties. 
	  In the future a more secure protocol will be implemented for CERTUS, using the same secure infrastructure.  
 	</div>
  </div>
</div>

<jsp:include page="footer.jsp" />
