<%@page import="service.HeaderService"%>

<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%
String messageAlert = (String) request.getAttribute("message_alert");
%>

<!--  <%=messageAlert %>-->

<div class="row">
<h4>About CERTUS</h4>
	<div class="large-8 medium-8 columns">
		CERTUS is a secure voting system developed at George Washington University.  It currently utilizes a 
		simple double envelope voting protocol that provides some basic privacy, integrity, and verifiability
		properties. In the future a more secure protocol will be implemented for CERTUS, using the same secure
		infrastructure.  
	</div>
</div>

<jsp:include page="footer.jsp" />