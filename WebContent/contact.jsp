<%@page import="service.HeaderService"%>

<jsp:include page="headerDefault.jsp" />

<% if(HeaderService.isAuthenticated()) { %>
  <jsp:include page="headerTopBarAuthenticated.jsp" /> 	
<% } else { %>
  <jsp:include page="headerTopBarDefault.jsp" />
<% } %>

<div class="row">
  <div class="large-12 medium-12 small-12 columns">
	<h4>About CERTUS</h4>
	<div class="global_body_text">
      CERTUS was developed under the guidance of Professor Michael Clarkson by Steven Frink, Dmitriy Karmazin, Ahmad Kouraiem, and Hirosh Wickramasuriya.
	  <br><br>
      <ul>
	    <li>Prof. Michael Clarkson: clarkson@gwu.edu</li>
		<li>Steven Frink: sfrink1@gmail.com</li>
		<li>Dmitriy Karmazin: dkarmazi@gwmail.gwu.edu</li>
		<li>Ahmad Kouraiem: ahmadko@gwmail.gwu.edu</li>
		<li>Hirosh Wickramasuriya: hirosh@gwmail.gwu.edu</li>
	  </ul>
 	</div>
  </div>
</div>

<jsp:include page="footer.jsp" />