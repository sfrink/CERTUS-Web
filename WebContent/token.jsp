<%@page import="service.HtmlService"%>
<%@page import="service.HeaderService"%>

<jsp:include page="headerDefault.jsp" />

<% if(HeaderService.isAuthenticated(request)) { %>
  <jsp:include page="headerTopBarAuthenticated.jsp" /> 	
<% } else { %>
  <jsp:include page="headerTopBarDefault.jsp" />
<% } %>

<div class="row">
  <div class="large-12 medium-12 small-12 columns">
	<h4>Download Certus Voting System Token</h4>
	<div class="global_body_text">
	  <p>
	    Please download and run this token to generate signatures for your votes.
	  </p>

	  <a class="tiny button radius" href="resources/token.jar">Download token.jar (~33Kb)</a>
	  <%= HtmlService.getToolTip("(How to run it?)", "To run token use: java -jar token.jar")%>

      <h5>Checksums</h5>
      
      <p style="word-wrap:break-word;">
        SHA512: 36d8bef4da830ce40451a3a26a42c8fe4063d6bb8cfe2281f9b612bf708d26178b58572448980078bad7cc9c8baa030bdb41791ae1823a45cd5d9f66017b17ed
	  </p>

      <p style="word-wrap:break-word;">
        MD5: cf32d820babf34f1e6450772db021c5f
	  </p>
 	</div>
  </div>
</div>

<jsp:include page="footer.jsp" />
