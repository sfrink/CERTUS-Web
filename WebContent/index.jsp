<jsp:include page="headerDefault.jsp" />

<% 
// redirect to login
RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");		
rd.forward(request, response);
%>

<jsp:include page="footer.jsp" />