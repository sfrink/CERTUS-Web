package servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import service.HeaderService;
import service.HtmlService;
import service.InvitedUserService;
import dto.UserDto;
import dto.Validator;

/**
 * Servlet implementation class InvitedUserServlet
 */

@WebServlet(name = "inviteduser", urlPatterns = { "/inviteduser" })
@MultipartConfig(maxFileSize = 10240)

public class InvitedUserServlet extends HttpServlet {
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
	private Part filePart = null;
	
	private String placeHoldFirstName = "Enter your first name here";
	private String placeHoldLastName = "Enter your last name here";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvitedUserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(!HeaderService.isTempUser(request)) {
			// if the user is not temp user, redirect to main.jsp
			messageAlert = HtmlService.drawMessageAlert("Select option to proceed", "");
			request.setAttribute("message_alert", messageAlert);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
		} else {
			//if the user is a temp user:
			
			messageAlert = HtmlService.drawMessageAlert("Complete your registration", "");
			
			//Prepare login screen
			routineUpdateNewUserModal(request);
			
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			request.setAttribute("message_alert", messageAlert);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/invitedUser.jsp");		
			rd.forward(request, response);
		}	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!HeaderService.isTempUser(request)) {
			
			// if the user is not temp user, redirect to main.jsp
			messageAlert = HtmlService.drawMessageAlert("Select option to proceed", "");
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
		}else{
			
			//Prepare login screen
			routineUpdateNewUserModal(request);
			
			if(request.getParameter("rdn_basic_signup") != null){
				//update new user with basic information:
				//get the values:
				firstName = request.getParameter("new_user_firstname");
				lastName = request.getParameter("new_user_lastname");
				emailAdd = HeaderService.getUserEmail(request);
				password = request.getParameter("new_user_password");
				
				//Do basic update:
				doBasicUpdate(request);
			}else if (request.getParameter("rdn_generate_new_keys") != null){
				//udpate new user with key protection password:
				//get the values:
				firstName = request.getParameter("new_user_firstname");
				lastName = request.getParameter("new_user_lastname");
				emailAdd = HeaderService.getUserEmail(request);
				password = request.getParameter("new_user_password");
				
				// get the protection password:
				keyPassword = request.getParameter("new_key_password");

				//Do update:
				doUpdateWithKeyProtectionPassword(request);
			}else if (request.getParameter("rdn_upload_key") != null){
				//update new user with uploaded key:
				//get the values:
				firstName = request.getParameter("new_user_firstname");
				lastName = request.getParameter("new_user_lastname");
				emailAdd = HeaderService.getUserEmail(request);
				password = request.getParameter("new_user_password");
				
				// obtains the upload file part in this multipart request
		        filePart = request.getPart("uploadFile");

		        //Do signup with pulic key:
				doUpdateWithPublicKey(request);
			}

			
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/invitedUser.jsp");
			rd.forward(request, response);			
		}
	}
	
	
	public void doBasicUpdate(HttpServletRequest request){
		resetGlobals();
		
		UserDto newUser = new UserDto();

		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(emailAdd);
		newUser.setPassword(password);
		
		Validator v = InvitedUserService.addUpdateUser(request, newUser);

		if (v.isVerified()){
			outModal = drawSuccessfullAdding();
		}else{
			outModal = drawFailedAdding(v.getStatus());
		}
		
	}
	
	/**
	 * This function tries to add new user to the DB and generate keys:
	 */
	public void doUpdateWithKeyProtectionPassword(HttpServletRequest request){
		resetGlobals();

		UserDto newUser = new UserDto();
		
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(emailAdd);
		newUser.setPassword(password);
		newUser.setTempPassword(keyPassword);
		
		Validator v = InvitedUserService.updateUserwithKeyProtectionPassword(request, newUser);

		if (v.isVerified()){
			outModal = drawSuccessfullAdding();
		}else{
			outModal = drawFailedAdding(v.getStatus());
		}
	}
	
	/**
	 * This function tries to add new user to the DB and upload the user public key:
	 * @throws IOException 
	 */	
	public void doUpdateWithPublicKey(HttpServletRequest request) throws IOException{
		resetGlobals();
		
		InputStream inputStream = null; // input stream of the upload file
        
        if (filePart != null) {
            int fileSize = (int) filePart.getSize();
            
            if (fileSize == 0){
            	outModal = drawUploadingPageError();
            }else if (fileSize >= 10240){
            	outModal = drawUploadingPageError();
            }else{
            	//obtains input stream of the upload file
            	inputStream = filePart.getInputStream();
            
            	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    			int nRead;
    			byte[] data = new byte[16384];

    			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
    			  buffer.write(data, 0, nRead);
    			}

    			buffer.flush();
    			
    			byte[] publicKeyBytes = buffer.toByteArray();
            	
            	UserDto newUser = new UserDto();
            	
        		newUser.setFirstName(firstName);
        		newUser.setLastName(lastName);
        		newUser.setEmail(emailAdd);
        		newUser.setPassword(password);
            	newUser.setPublicKeyBytes(publicKeyBytes);
            	
            	Validator res = InvitedUserService.updateUserWithPublicKey(request, newUser);
          
            	if (res.isVerified()){
            		outModal = drawSuccessfullAdding();
                }else{
                	outModal = drawFailedAdding(res.getStatus());
                }
            }
        }

	}
	

	
	
	/**
	 * This function is to draw a welcome page after a successful adding user:
	 * @return HTML String
	 */
	public String drawSuccessfullAdding(){
		String out = "";
		
		out += "<div class=\"row\">";
			out += "<h3>Welcome " + firstName + " to Certus Voting System!</h3>";
			out += "<a class=\"button radius\" href=\"login\">Let's get started!</a>";
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
			out += "<h3>OOPS!</h3>";
			out += "<h3>We couldn't create a new account for you because "+ status +"</h3>";
			out += "<h3>Do you want to try again?</h3>";
		
			out += "<ul class=\"button-group\">";
				out += "<li><a href=\"inviteduser\" class=\"button radius\">Try again</a></li>";
				out += "<li><a href=\"login\" class=\"button radius\">Return to Login Page</a></li>";
			out += "</ul>";
		out += "</div>";
		
		return out;	
	}
		
    public String drawUploadingPageError(){
		String out = "";
		out += "<h5><font color=\"red\">You didn't select a file!</font></h5>";
		out += "<h5>";
		out += "Your file size cannot be larger than 10 kilbytes.";
		out += "</h5>";
		out += "<h5>";
		out += "<div class=\"row\">";
		out += "<a href=\"signup\">Start again</a>";
		out += "</div>";

		return out;
	}

		
	/**
	 * This function performs required actions to show add new new modal
	 */
	public void routineUpdateNewUserModal(HttpServletRequest request) {	
		resetGlobals();
		outModal = drawUpdateUser(request);
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
	public String drawUpdateUser(HttpServletRequest request) {
		String out = "";
		
		out += "<form id=\"invited_form_user_new\" name=\"invited_form_user_new\" action=\"inviteduser\" method=\"post\" data-abide enctype=\"multipart/form-data\" onsubmit=\"return prepareInvitedForm()\" >"; 
		// draw basic user info
		out += "<div class=\"row\">";
			out += "<div class=\"large-6 medium-6 columns\">";
				out += "<fieldset>";
					out += "<legend>Basic Information</legend>";	
					out += HtmlService.drawInputTextAlphanumeric("new_user_firstname", "First Name", placeHoldFirstName, "");
					out += HtmlService.drawInputTextAlphanumeric("new_user_lastname", "Last Name", placeHoldLastName, "");
					
					out += "<div class=\"" + "new_user_email" + "\">";
					out += "<label>" + "E-mail Address" + " <small>required</small>";
					out += "<input disabled=\"none\" type=\"email\" name=\""+ "new_user_email" + "\" placeholder=\"" + "your@email.address" + "\" value=\"" + HeaderService.getUserEmail(request) + "\" required \">";
					out += "</label>";
					out += "<small class=\"error\">" + "E-mail Address" + " field can only contain letters and numbers and cannot be empty</small>";
					out += "</div>";
					
					out += HtmlService.drawInputTextPasswordAndConfirmation("new_user_password", "Password", "new_user_password_confirm", "Confirm Password", false);
					out += "Note: this password will be used to protect your private key, if you want to setup a different "
								+ "password to protect your private key, or upload your own public key, click advanced options.";
					out += "<p>";
					out += "<a href=\"#\" onMouseDown=\"showAdvanced()\" id=\"show_advanced_options\">(Show Advanced Options)</a>";
					out += "<a href=\"#\" onMouseDown=\"hideAdvanced()\" id=\"hide_advanced_options\" style=\"display: none\">(Hide Advanced Options)</a>";
					out += "</p>";
					
				out += "</fieldset>";

				// buttons
				out += "<p>";
				out += "<button class=\"radius button left\" type=\"submit\" name=\"button_basic_signup\">Update</button>";
				out += "</p>";

			out += "</div>";
			
			// RIGHT
			out += "<div class=\"large-6 medium-6 columns panel clear\" id=\"div_generate_key\" style=\"display: none\" data-equalizer-watch>";
			//Basic Signup Radio:
			out += "<input type=\"radio\" name=\"rdn_basic_signup\" id=\"rdn_basic_signup\" value=\"rdn_basic_signup\" onMouseDown=\"onBasicSignupRadioClick()\" checked><label onMouseDown=\"onBasicSignupRadioClick()\" for=\"rdn_basic_signup\">Basic Information Only</label><br>";
			
			//Generate New key Radio:
			out += "<input type=\"radio\" name=\"rdn_generate_new_keys\" id=\"rdn_generate_new_keys\" value=\"rdn_generate_new_keys\" onMouseDown=\"onGenerateKeysRadioClick()\"><label onMouseDown=\"onGenerateKeysRadioClick()\" for=\"rdn_generate_new_keys\">Generate new protected private key</label><br>";
		
			//draw key protection fields:
			out += "<div>";
				out += "<fieldset>";
					out += "<legend>Key Protection Password</legend>";
					out += HtmlService.drawInputTextPasswordAndConfirmation("new_key_password", "Password", "new_key_password_confirm", "Confirm Password", true);

					//button
					out += "<p>";
					out += "Once you hit the submit button, we will create an account for you and send your protected private key to your email address.";
					out += "P.S. this might take few seconds, please wait.";
					out += "</p>";
				out += "</fieldset>";
			out += "</div>";
		out += "</div>";

		//Upload Old public key Radio:
	
		out += "<div class=\"row\" id=\"div_upload_key\" style=\"display: none\">";
			out += "<input type=\"radio\"  onMouseDown=\"onUploadRadioClick()\" name=\"rdn_upload_key\" id=\"rdn_upload_key\" value=\"rdn_upload_key\" ><label onMouseDown=\"onUploadRadioClick()\" for=\"rdn_upload_key\">Upload your own public key</label><br>";
			//Draw upload button
			out += "<div class=\"large-6 medium-6 columns\">";
				out += "<fieldset>";
					out += "<legend>Your Public Key path</legend>";
					
					//error messages:
					out += "<div id=\"emptyFileError\" style=\"display: none\"><p><font color=\"red\">Please select a file.</font></p></div>";
					out += "<div id=\"largeFileError\" style=\"display: none\"><p><font color=\"red\">The file is larger than 10 kilobytes.</font></p></div>";
					out += "<div id=\"apiFileError\" style=\"display: none\"><p><font color=\"red\">The file API isn't supported on this browser yet.</font></p></div>";
					
					out += "<p>Your file size cannot be larger than 10 kilobytes.</p>";
					
					out += "<input name=\"uploadFile\" id=\"FileInput\" type=\"file\" size=\"10240\" disabled=\"disabled\">";
					out += "<input type=\"hidden\" name=\"user_firstName\" value=\"" + firstName + "\">";
					out += "<input type=\"hidden\" name=\"user_lastName\" value=\"" + lastName + "\">";
					out += "<input type=\"hidden\" name=\"user_email\" value=\"" + emailAdd + "\">";
					out += "<input type=\"hidden\" name=\"user_password\" value=\"" + password + "\">";
					out += "<input type=\"hidden\" name =\"testtest\" value =\"testtest\" >";

				out += "</fieldset>";
			out += "</div>";		
		out += "</div>";
		out += "</div>";	
	out += "</form>";
		
		
		return out; 
	}

	
}
