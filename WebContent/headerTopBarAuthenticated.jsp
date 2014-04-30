<%@page import="service.HeaderService"%>

<nav class="top-bar" data-topbar="">
    <ul class="title-area">
      <!-- Title Area -->
      <li class="name">
        <h1>
          <a href="login">CERTUS VOTING SYSTEM</a>
        </h1>
      </li>
      <li class="toggle-topbar menu-icon"><a href="#"><span>menu</span></a></li>
    </ul>

  <section class="top-bar-section">
      <!-- Right Nav Section -->
      <ul class="right">

        <li class="divider"></li>
        <li><a href="token.jsp">Download Token</a></li>

        <li class="divider"></li>
        <li class="has-dropdown not-click">
          <a href="#">More</a>
          <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>
            <li><a href="about.jsp">About us</a></li>
            <li><a href="contact.jsp">Contact</a></li>
            <li><a href="http://foundation.zurb.com/docs/components/kitchen_sink.html">Help on HTML</a></li>
          </ul>
        </li>

        <li class="divider"></li>
        <li class="has-dropdown not-click">
          <a href="#">Settings</a>
          <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>

			<% if(HeaderService.hasAccess(request, "profile")) { %>
			  <li><a href="profile">Edit Profile</a></li>
			<% } %>

			<% if(HeaderService.hasAccess(request, "newkey")) { %>
			  <li><a href="newkey">Generate New Key</a></li>
			<% } %>

			<% if(HeaderService.hasAccess(request, "upload")) { %>
			  <li><a href="uploadkeypage">Upload Public Key</a></li>
			<% } %>
          </ul>
        </li>

        <li class="divider"></li>
        <li class="has-form">
	        <form action="login" method="post">
    		    <button class="radius button" type="submit" name="logout">Logout</button>
			</form>    		    
    	</li>
      </ul>
   </section>
 </nav>
    
 <div class="global_margin_bottom"></div>