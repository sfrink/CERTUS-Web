<jsp:include page="headerDefault.jsp" /> 

<!-- Main Content Section -->
<!-- This has been source ordered to come first in the markup (and on small devices) but to be to the right of the nav on larger screens -->
  

  <div class="row">
  
      <div class="large-9 push-3 columns">
  
  
    <h3>Welcome to CERTUS!</h3>
    <br>
	<h3><small>Please login or register before using the system1</small></h3>
    <br>
	      
   	<form action="login" method="post">
		<table>
			<tr>
				<td>Username</td>
				<td><input type="text" name="username" value="dkarmazi" /></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password" value="cdsnj234qASdcd223" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Login"></td>
			</tr>			
		</table>
	</form>
	</div>
	

Output	
	<%
	

	out.print(request.getAttribute("atr"));
	
	%>
 Enf of Output

<jsp:include page="navBarLeft.jsp" />
<jsp:include page="footer.jsp" />  