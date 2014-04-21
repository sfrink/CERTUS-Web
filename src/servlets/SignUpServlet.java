package servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dto.ElectionDto;
import dto.UserDto;
import dto.Validator;
import service.HeaderService;
import service.HtmlService;
import service.NewKeyService;
import service.SignUpService;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet(name = "signup", urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	
	private String firstName = "";
	private String lastName = "";
	private String emailAdd = "";
	private String password = "";
	private String keyPassword = "";
	
	
	private String placeHoldFirstName = "Enter your first name here";
	private String placeHoldLastName = "Enter your last name here";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(HeaderService.isAuthenticated()) {
			// logged in, redirect to main
			messageAlert = HtmlService.drawMessageAlert("Select option to proceed", "");
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
		} else {
			// user came for the first time, prepare login screen
			routineAddNewUserModal();
			messageAlert = HtmlService.drawMessageAlert("New User Wizard", "");
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			request.setAttribute("message_alert", messageAlert);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/signUp.jsp");		
			rd.forward(request, response);
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(HeaderService.isAuthenticated()) {
			// logged in, redirect to main
			messageAlert = HtmlService.drawMessageAlert("Select option to proceed", "");
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
		}else{
			
			if (request.getParameter("show_advanced") != null){
				//forward parameters:
				firstName = request.getParameter("new_user_firstname");
				lastName = request.getParameter("new_user_lastname");
				emailAdd = request.getParameter("new_user_email");
				password = request.getParameter("new_user_password");
				
				
				//draw advanced options page:
				routineShowAdvancedModal();
			}else if (request.getParameter("button_basic_signup") != null){
				//create new user with basic information:
				//get the values:
				firstName = request.getParameter("new_user_firstname");
				lastName = request.getParameter("new_user_lastname");
				emailAdd = request.getParameter("new_user_email");
				password = request.getParameter("new_user_password");
				
				//Do basic signup:
				doBasicSignUp();
			}else if (request.getParameter("generate_and_submit_button") != null){
				//create new user with key protection password:
				// get the protection password:
				keyPassword = request.getParameter("new_key_password");
				//Do signup:
				doSignUpWithKeyProtectionPassword();
			}
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/signUp.jsp");
			rd.forward(request, response);			
		}
	}
	
	
	public void doBasicSignUp(){
		resetGlobals();
		
		UserDto newUser = new UserDto();

		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(emailAdd);
		newUser.setPassword(password);
		
		Validator v = SignUpService.addBasicUser(newUser);

		if (v.isVerified()){
			outModal = drawSuccessfullAdding();
		}else{
			outModal = drawFailedAdding(v.getStatus());
		}
		
	}
	
	/**
	 * This function tries to add new user to the DB and generate keys:
	 */
	public void doSignUpWithKeyProtectionPassword(){
		resetGlobals();

		UserDto newUser = new UserDto();
		
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(emailAdd);
		newUser.setPassword(password);
		newUser.setTempPassword(keyPassword);
		
		Validator v = SignUpService.addUserwithKeyProtectionPassword(newUser);

		if (v.isVerified()){
			outModal = drawSuccessfullAdding();
		}else{
			outModal = drawFailedAdding(v.getStatus());
		}
	}
	
	/**
	 * This function is to draw a welcome page after a successful adding user:
	 * @return HTML String
	 */
	public String drawSuccessfullAdding(){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>Welcome " + firstName + " to Certus Voting System!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<a class=\"button radius\" href=\"login\">Let's get started!</a>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		
		return out;
	}
	
	/**
	 * This function is to draw a failure page after an unsuccessful adding user:
	 * @return HTML String
	 */
	public String drawFailedAdding(String status){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>OOPS!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>We couldn't create a new account for you because "+ status +"</h3>";
		out += "<h3>Do you want to try again?</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<a href=\"signup\" class=\"button radius\">Try again</a>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<a href=\"login\" class=\"button radius\">Return to Login Page</a>";
		out += "</div>";
		out += "</div>";
		
		return out;	
	}
	
	/**
	 * This function performs required actions to show add new new modal
	 */
	public void routineAddNewUserModal() {	
		resetGlobals();
		outModal = drawNewUser();
	}

	
	public void routineShowAdvancedModal(){
		resetGlobals();
		messageAlert = HtmlService.drawMessageAlert("Advanced Options", "");
		outModal = drawAdvancedOptions();
	}
	
	
	public String drawAdvancedOptions(){
		String out = "";
		
		out += "<form id=\"advanced_form\" action=\"signup\" method=\"post\" data-abide>";
			out += "<div class=\"row\">";
	
				//Generate New key Radio:
				out += "<input type=\"radio\" name=\"KeyGeneratingType\" id=\"rdn_generate_new_keys\" value=\"generate_new_keys\" onMouseDown=\"onGenerateKeysRadioClick()\" checked>Generate new protected private key<br>";
		
				// draw key protection fields:
				out += "<div class=\"large-6 medium-6 columns\">";
					out += "<fieldset>";
						out += "<legend>Key Protection Password</legend>";
						out += HtmlService.drawInputTextPasswordAndConfirmation("new_key_password", "Password", "new_key_password_confirm", "Confirm Password");

						// button
						out += "Once you hit the submit button, we will create an account for you and send your protected private key to your email address.";
						out += "P.S. this might take few seconds, please wait.";
						out += "<div class=\"row\">";
							out += "<div class=\"row\">";
								out += "<button id=\"btn_generate_new_keys\"class=\"radius button right\" type=\"submit\" name=\"generate_and_submit_button\">Generate and Submit</button>";
							out += "</div>";
						out += "</div>";
					out += "</fieldset>";
				out += "</div>";
			out += "</div>";
		out += "</form>";
		
		
		//Upload Old public key Radio:
		out += "<form action=\"signupwithkey\" method=\"post\" enctype=\"multipart/form-data\" onsubmit=\"return showFileSize()\" >";
			out += "<div class=\"row\">";
		
				out += "<input type=\"radio\"  onMouseDown=\"onUploadRadioClick()\" name=\"KeyGeneratingType\" id=\"upload_old_key\" value=\"upload_old_key\" >Upload your own public key<br>";
		
				//Draw upload button
				out += "<div class=\"large-6 medium-6 columns\">";
					out += "<fieldset>";
						out += "<legend>Your Public Key path</legend>";
						
						//error messages:
						out += "<div id=\"emptyFileError\" style=\"display: none\"><p><font color=\"red\">Please select a file.</font></p></div>";
						out += "<div id=\"largeFileError\" style=\"display: none\"><p><font color=\"red\">The file is larger than 10 bytes.</font></p></div>";
						out += "<div id=\"apiFileError\" style=\"display: none\"><p><font color=\"red\">The file API isn't supported on this browser yet.</font></p></div>";
						
						out += "Your file size cannot be larger than 10 bytes.";
						out += "<div class=\"row\">";
				
							out += "<div><input name=\"uploadFile\" id=\"FileInput\" type=\"file\" size=\"10240\" disabled=\"disabled\"></div>";
							out += "<div>";
								out += "<input type=\"hidden\" name=\"user_firstName\" value=\"" + firstName + "\">";
								out += "<input type=\"hidden\" name=\"user_lastName\" value=\"" + lastName + "\">";
								out += "<input type=\"hidden\" name=\"user_email\" value=\"" + emailAdd + "\">";
								out += "<input type=\"hidden\" name=\"user_password\" value=\"" + password + "\">";
								out += "<button id=\"button_start_uploading\" type=\"submit\" value=\"Upload\" disabled=\"disabled\" class=\"radius button right\" name=\"upload_and_submit_button\">Upload and Submit</button>";
							out += "</div>";
						out += "</div>";
				out += "</fieldset>";
			out += "</div>";		
		out += "</div>";
	out += "</form>";
	
	
		return out;
	}
	
	
	
	
	/**
	 * This function performs required actions to show private key protection modal
	 */
	public void routineKeyProtectionModal(){
		mode = "2";
		messageLabel = HtmlService.drawMessageLabel("Please fill in required labels", "secondary");
		outModal = drawKeyProtection();
//		outModal += request.getParameter("new_user_firstname");
		
	}
	
	
	/**
	 * This functions resets all global variables for this class
	 */
	private void resetGlobals() {
		this.messageAlert = "";
		this.mode = "1";
		messageLabel = "";
		outModal = "";
	}

	
	/**
	 * 
	 * This method draws HTML modal output for a new user
	 * @return HTML output
	 */
	public String drawNewUser() {
		String out = "";
		
		out += "<form id=\"form_user_new\" action=\"signup\" method=\"post\" data-abide>";
		
		// draw new user info
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += HtmlService.drawInputTextAlphanumeric("new_user_firstname", "First Name", placeHoldFirstName, "");
		out += HtmlService.drawInputTextAlphanumeric("new_user_lastname", "Last Name", placeHoldLastName, "");
		out += HtmlService.drawInputTextEmail("new_user_email", "E-mail Address", "your@email.address", "");
		out += HtmlService.drawInputTextPasswordAndConfirmation("new_user_password", "Password", "new_user_password_confirm", "Confirm Password");
		out += "Note: this password will be used to protect your private key, if you want to setup a different "
				+ "password to protect your private key, please go to";
		out += "<button class=\"label radius\" type=\"submit\" name=\"show_advanced\">advanced options</button>"; 
		// button
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"button_basic_signup\">Sign Up</button>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "</form>";
		
		
		return out; 
	}

	
	/**
	 * This method draws HTML modal output for key protection
	 * @return HTML output
	 */
	public String drawKeyProtection(){
		String out = "";
		
		out += "<h5>New User Wizard</h5>";
		out += "<form id=\"form_user_new\" action=\"signup\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw key protection fields:
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Key Protection Password</legend>";
		out += HtmlService.drawInputTextPasswordAndConfirmation("new_key_password", "Password", "new_key_password_confirm", "Confirm Password");
		// button
		out += "<div class=\"row\"><h5>Once you hit the submit button, we will create an account for you and send your protected private key to your email address.</h5></div>";
		out += "<div class=\"row\"><h5>P.S. this might take few seconds, please wait.</h5></div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"submit_and_generate\">Submit</button>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "</fieldset>";
		out += "</form>";
		
		return out; 
		
	}
	
	
}
