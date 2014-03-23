<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%
	String allElections = (String) request.getAttribute("all_elections");




%>






<div class="row">
	<div class="large-12 columns">

    	<h3>Voting Screen | Authenticated User</h3>

	<div class="row">
		<%=allElections %>
	
		<span>Line1</span>
    </div>
	<div class="row">
		<span>Line2</span>
    </div>
		





	</div>
</div>
















<jsp:include page="footer.jsp" />
