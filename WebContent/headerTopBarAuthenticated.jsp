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
        <li class="has-dropdown not-click">
          <a href="#">Options</a>
          <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>
            <li><a href="#">About the project</a></li>
            <li><a href="#">Contact Us</a></li>
            <li><a href="http://foundation.zurb.com/docs/components/kitchen_sink.html">Help on HTML</a></li>
          </ul>
        </li>

        <li class="divider"></li>
        <li class="has-dropdown not-click">
          <a href="#">My profile</a>
          <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>

			<% if(HeaderService.hasAccess("profile")) { %>
			  <li><a href="profile">Edit</a></li>
			<% } %>

			<% if(HeaderService.hasAccess("newkey")) { %>
			  <li><a href="newkey">Key Management</a></li>
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