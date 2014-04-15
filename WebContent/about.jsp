<%@page import="service.HeaderService"%>
<%
if(!HeaderService.isAuthenticated()) {
 	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);		
}
%>

<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%
String messageAlert = (String) request.getAttribute("message_alert");
%>

<!--  <%=messageAlert %>-->
<h1>About CERTUS</h1>
<div>
CERTUS is a secure voting system developed at George Washington University.  It currently utilizes a 
simple double envelope voting protocol that provides some basic privacy, integrity, and verifiability
properties.  In the future a more secure protocol will be implemented for CERTUS, using the same secure
infrastructure.  
</div>

<jsp:include page="footer.jsp" />