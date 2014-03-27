<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" /> 

<%
String messageAlert = (String) request.getAttribute("message_alert");

String username = request.getAttribute("username") != null ? (String) request.getAttribute("username") : "user@certus.org";
String password = "password";
%>

<%=messageAlert %>
  
  
<div class="row">
  <div class="large-6 medium-6 columns">
<form action="login" method="post">
  <fieldset>
    <legend>Authorization</legend>

    <div class="row">
      <div class="large-6 medium-6 columns">
      	<label>Email</label>
		<input type="text" name="username" value="<%=username%>"/>      
      </div>
    </div>

    <div class="row">
      <div class="large-6 medium-6 columns">
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