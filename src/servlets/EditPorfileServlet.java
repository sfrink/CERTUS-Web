package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.UserDto;
import dto.Validator;
import service.EditProfileService;
import service.HeaderService;
import service.HtmlService;

/**
 * Servlet implementation class EditPorfileServlet
 */
@WebServlet(name = "profile", urlPatterns = { "/profile" })
public class EditPorfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	private String outUser = "";
	
	private String firstName = "";
	private String lastName = "";

	private String currentPassword = "";
	private String newPassword = "";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditPorfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (HeaderService.isTempUser(request)){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
			rd.forward(request, response);
		} else if(HeaderService.isAuthenticated(request)) {
			resetGlobals();
			routineExistingUser(request);
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_user", outUser);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/editProfile.jsp");
			rd.forward(request, response);		
		}
		else{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(HeaderService.isAuthenticated(request)) {
			resetGlobals();

			if (request.getParameter("button_edit") != null) {
				 //SHOW EDIT USER SCREEN
				routineEditUserModal();
			}else if (request.getParameter("save_edit_user") != null){
				//Update user Info:
				updateUserInfo(request);
			}else if(request.getParameter("button_change_password") != null){
				routineChangePassword();
			}else if (request.getParameter("button_update_password") != null){
				updateUserPassword(request);
			}
			// refresh 
			routineExistingUser(request);

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_user", outUser);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/editProfile.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}

	private void resetGlobals() {
		this.mode = "1";
		this.messageAlert = "";
		this.messageLabel = "";
		this.outModal = "";
		this.outUser = "";
	}


	public String drawLoginFieldset(UserDto u) {
		String out = "";
		if(u == null) {
			u = new UserDto();
			u.setEmail("user@certus.org");
			u.setPassword("password");
		}
		
		out += "<form action=\"login\" method=\"post\" data-abide>";
		out += "<fieldset>";
		out += "<legend>Authorization</legend>";
		out += HtmlService.drawInputTextEmail("username", "Email", "your@email.address", u.getEmail());		
		out += HtmlService.drawInputTextPassword("password", "Password", "password", u.getPassword(), false, "");
		out += "<input type=\"submit\" name=\"login\" class=\"small radius button\" value=\"Login\">";
		out += "<p><a href=\"signup\">New User?</a></p>";
		out += "</fieldset>";
		out += "</form>";
		
		return out;
	}
	
	
	public void updateUserPassword(HttpServletRequest request) throws ServletException, IOException{
		UserDto newUser = new UserDto();
		
		currentPassword = request.getParameter("current_password");
		newPassword = request.getParameter("new_password");
		
		newUser.setPassword(currentPassword);
		newUser.setTempPassword(newPassword);
				
		Validator v = EditProfileService.updatePassword(request, newUser);

		if (v.isVerified()){
			mode = "2";
			outModal = drawSuccessfullPasswordUpdate();
			messageAlert = "";
		}else{
			mode = "2";
			outModal = drawFailedPasswordUpdate(v.getStatus());
			
		}
	}
	
	
	public void updateUserInfo(HttpServletRequest request) throws ServletException, IOException{
		UserDto newUser = new UserDto();
		
		firstName = request.getParameter("edit_user_first_name");
		lastName = request.getParameter("edit_user_last_name");
		
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
				
		Validator v = EditProfileService.editUser(request, newUser);

		if (v.isVerified()){
			mode = "2";
			outModal = drawSuccessfullUpdate();
		}else{
			mode = "2";
			outModal = drawFailedUpdate(v.getStatus());
		}
	}

	public String drawSuccessfullPasswordUpdate(){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>Your password was updated successfully.</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<a href class=\"button radius\">Done</a>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		
		return out;
	}
	
	public String drawFailedPasswordUpdate(String status){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>OOPS!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>We couldn't update your password, and that's because: "+ status +"</h3>";
		out += "<h3>Do you want to try again?</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<form action=\"profile\" method=\"post\">	";
		out += "<button class=\"button radius\" type=\"submit\" name=\"button_change_password\" value=\"new\">Yes, let's try again</button>		";
		out += "</form>";
		out += "<a href=\"profile\">No, just forget about it.</a>";
		out += "</div>";
		out += "</div>";
		
		return out;	
	}
		
	
	public String drawSuccessfullUpdate(){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>Your information was updated successfully !</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<a class=\"button radius\" href=\"login\">Done</a>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		
		return out;
	}
	
	public String drawFailedUpdate(String status){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>OOPS!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>We couldn't update your account information, and that's because: "+ status +"</h3>";
		out += "<h3>Do you want to try again?</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<form action=\"profile\" method=\"post\">	";
		out += "<button class=\"button radius\" type=\"submit\" name=\"button_edit\" value=\"new\">Yes, let's try again</button>		";
		out += "</form>";
		out += "<a href=\"profile\">No, just forget about it.</a>";
		out += "</div>";
		out += "</div>";
		
		return out;	
	}
	

	public void routineChangePassword() {	
		resetGlobals();
		mode = "2";
		outModal = drawChangePassword();
	}

	public String drawChangePassword() {
		String out = "";

		out += "<h5>Change Password</h5>";
		
		out += "<form id=\"form_password_change\" action=\"profile\" method=\"post\" data-abide>";
			out += "<div class=\"row\">";
			// draw key protection fields:
				out += "<div class=\"large-6 medium-6 columns\">";
					out += "<fieldset>";
						out += HtmlService.drawInputTextPassword("current_password", "Current Password", "Your Current Password", "", false, "");
						out += HtmlService.drawInputTextPasswordAndConfirmation("new_password", "Your New Password", "new_password_confirm", "Confirm New Password");
						// button
						out += "<button class=\"radius button left\" type=\"submit\" name=\"button_update_password\">Change Password</button>";
					out += "</fieldset>";
				out += "</div>";
			out += "</div>";
		out += "</form>";

		return out;
	}

	
	public void routineEditUserModal() {	
		resetGlobals();
		mode = "2";
		outModal = drawEditUser();
	}
	
	
	public String drawEditUser() {
		String out = "";

		out += "<h5>Edit Profile</h5>";
		out += "<form id=\"form_user_edit\" action=\"profile\" method=\"post\" data-abide>";
			out += "<div class=\"row\">";
		// 	draw user info
				out += "<div class=\"large-6 medium-6 columns\">";
					out += "<fieldset>";
						out += "<legend>User Information</legend>";
						out += HtmlService.drawInputTextAlphanumeric("edit_user_first_name", "First Name", "Enter First Name", firstName);
						out += HtmlService.drawInputTextAlphanumeric("edit_user_last_name", "Last Name", "Enter Last Name", lastName);
						// button
						out += "<button class=\"radius button left\" type=\"submit\" name=\"save_edit_user\">Update Info</button>";
					out += "</fieldset>";
				out += "</div>";
			out += "</div>";
		out += "</form>";

		return out;
	}
	
	public void routineExistingUser(HttpServletRequest request) {
		// get the user info from DB
		UserDto userInfo = new UserDto();
		Validator v = EditProfileService.selectUser(request, HeaderService.getUserId(request));
		
		if(v.isVerified()) {
			userInfo = (UserDto) v.getObject();	
		} else {
			messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert") ;
		}
		
		
		firstName = userInfo.getFirstName();
		lastName = userInfo.getLastName();
		
		outUser = drawExistingUser(userInfo);
	}	

	public String drawExistingUser(UserDto user) {
		String out = "";
				
		if(user != null) {
			out += "<form action=\"profile\" method=\"post\">";
			
			out += "<div class=\"row\">";
			out += "<h3>Your Personal Information:</h3>";
			out += "</div>";
			
			out += "<div class=\"row\">";
			out += "<p><b>First Name:</b>\t" + user.getFirstName() + "</p>";
			out += "<p><b>Last Name:</b>\t" + user.getLastName() + "</p>";
			out += "<p><b>E-mail Address:</b>\t" + user.getEmail() + "</p>";
			out += "</div>";
			
			out += "<div class=\"row\">";
			out += "<h3>";
			out += "<button class=\"button radius\" type=\"submit\" name=\"button_edit\" value=\"new\">Edit Information</button>	";
			out += "<button class=\"button radius\" type=\"submit\" name=\"button_change_password\" value=\"new\">Change Password</button>";
			out += "</h3>";
			out += "</div>";
					
			out += "</form>";
			
		} else {
			out += "<div class=\"label secondary\">Couldn't fitch user information</div>";
		}
		
		return out;
	}

	
	
}
