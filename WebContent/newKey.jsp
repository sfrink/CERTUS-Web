<%@page import="service.HeaderService"%>
<%
if(!HeaderService.isAuthenticated() ||
   !HeaderService.hasAccess("newkey")) {
	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);
}

// get all data from previous screen
String mode = (String) request.getAttribute("mode");
String messageAlert = (String) request.getAttribute("message_alert");
String messageLabel = (String) request.getAttribute("message_label");
String outModal = (String) request.getAttribute("out_modal");
%>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />

<!-- display a message -->
<%=messageAlert %> 

	
		<div class="row">
		
		<h3>Generate New Private Key</h3>
			<%=outModal %>
		</div>


<jsp:include page="footer.jsp" />  