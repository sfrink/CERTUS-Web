<%@page import="service.HeaderService"%>
<%
if(HeaderService.isAuthenticated(request)) {
	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);
}
%>

<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" /> 

<%
String messageAlert = (String) request.getAttribute("message_alert");
String outForm = (String) request.getAttribute("out_form");
%>

<%=messageAlert %>
  
<div class="row">
	<div class="large-6 medium-6 columns">
		<%=outForm %>
	</div>
</div>


<jsp:include page="footer.jsp" />  