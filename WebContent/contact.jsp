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
<h1>Contact Us</h1>
<div>
CERTUS was developed under the guidance of Prof. Michael Clarkson by Steven Frink, Dmitriy Karmazin, Ahmad Kouraiem, and Hirosh Wickramasuriya.

<ul>

	<li>Prof. Michael Clarkson: clarkson@gwu.edu</li>

	<li>Steven Frink: sfrink1@gmail.com</li>
	<li>Dmitriy Karmazin: dkarmazi@gwmail.gwu.edu</li>
	<li>Ahmad Kouraiem: ahmadko@gmail.com</li>
	<li>Hirosh Wickramasuriya: hirosh@gwmail.gwu.edu</li>
</ul>
</div>

<jsp:include page="footer.jsp" />