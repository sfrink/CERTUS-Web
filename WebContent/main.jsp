<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarAuthenticated.jsp" /> 

<%
String state = (String) request.getAttribute("state");
String message = "";
String styleAlertBox = "alert-box radius";

if(state != null && state.equals("success")) {
	styleAlertBox = "alert-box success radius";
	message = (String) request.getAttribute("message");
}
%>

<div data-alert class="<%=styleAlertBox %>">
    <b><%=message %></b>
  <a href="" class="close">×</a>
</div>



<div class="row">
  <div class="large-12 columns">

	<h3>Welcome Screen | Authorized User</h3>

	<div class="row">
	  <a href="election" class="button radius">Add / Edit Elections</a>
	</div>
	
	<div class="row">  
		<a href="voting" class="button radius">Vote</a>
	</div>
	
	<div class="row">
		<a href="tallying" class="button radius">Tallying</a>
	</div>
	
  </div>
</div>




<jsp:include page="footer.jsp" />