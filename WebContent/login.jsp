<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" /> 

<%
String messageAlert = (String) request.getAttribute("message_alert");
String outForm = (String) request.getAttribute("out_form");

String messageErrorRmi = "";
if(request.getAttribute("rmi_error") != null) {
	messageErrorRmi = (String) request.getAttribute("rmi_error");
}
%>

<%=messageErrorRmi %>
<%=messageAlert %>
  
  
<div class="row">
	<div class="large-6 medium-6 columns">
		<%=outForm %>
	</div>

	<div class="large-6 medium-6 columns">
	  <div class="panel clearnb">
		<blockquote>
		CERTUS is a secure voting system developed at George Washington University. It currently utilizes a simple double envelope voting protocol that provides some basic privacy, integrity, and verifiability properties. In the future a more secure protocol will be implemented for CERTUS, using the same secure infrastructure.
		</blockquote>
	  </div>
	</div>



</div>



<jsp:include page="footer.jsp" />  