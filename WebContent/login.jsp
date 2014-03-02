<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" /> 


<%

String username = request.getAttribute("username") != null ? (String) request.getAttribute("username") : "jgalt@jj.com";
String password = "cdsnJewqq1";


String state = (String) request.getAttribute("state");
String message = "Welcome to CERTUS! Please login or register before using the system";
String styleAlertBox = "alert-box radius";

if(state != null && state.equals("fail")) {
	styleAlertBox = "alert-box alert radius";
	message = (String) request.getAttribute("message");
}
%>


<div data-alert class="<%=styleAlertBox %>">
    <b><%=message %></b>
  <a href="" class="close">×</a>
</div>
  
  
<div class="row">
  <div class="large-6 columns">
<form action="login" method="post">
  <fieldset>
    <legend>Authorization</legend>

    <div class="row">
      <div class="large-6 columns">
      	<label>Email</label>
		<input type="text" name="username" value="<%=username%>"/>      
      </div>
    </div>

    <div class="row">
      <div class="large-6 columns">
		<label>Password</label>
		<input type="password" name="password" value="<%=password%>"/>
      </div>
    </div>

    <input type="submit" class="small radius button" value="Login">

  </fieldset>
</form>
</div>
</div>


<jsp:include page="footer.jsp" />  