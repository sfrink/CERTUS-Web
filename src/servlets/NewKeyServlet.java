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
import service.HeaderService;
import service.HtmlService;
import service.NewKeyService;


/**
 * Servlet implementation class NewKeysServlet
 */
@WebServlet("/newkey")
public class NewKeyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	
	private String keyPassword = "";
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewKeyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(HeaderService.isAuthenticated()) {
			resetGlobals();
			// Redirect
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/newKey.jsp");
			rd.forward(request, response);			
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(HeaderService.isAuthenticated()) {
			resetGlobals();
			
			if(request.getParameter("button_start") != null) {
				routineNewKeysModal();
			}else if (request.getParameter("button_generate") != null) {
				keyPassword = request.getParameter("new_key_password");	
				generateNewKey();
			}

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/newKey.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}

	}

	/**
	 * This function tries to generate new private key:
	 */
	public void generateNewKey(){
		resetGlobals();
		mode = "2";
		
		int userID = HeaderService.getUserId();
		String sessionID = HeaderService.getUserSessionId();
		
		Validator v = NewKeyService.generateNewKeys(userID, keyPassword, sessionID);
		
		if (v.isVerified()){
			outModal = drawSuccessfullGenerating();
		}else{
			outModal = drawFailedGenerating(v.getStatus());
		}
	}

	
	/**
	 * This function is to draw a welcome page after a successful generating new key:
	 * @return HTML String
	 */
	public String drawSuccessfullGenerating(){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>Your new protected key was generated successfully and sent to your e-mail.</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<a class=\"button radius\" href=\"login\">Finish</a>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		
		return out;
	}
	
	/**
	 * This function is to draw a failure page after an unsuccessful generating new key:
	 * @return HTML String
	 */
	public String drawFailedGenerating(String status){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>OOPS!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>We couldn't create a new protected key for you, and that's because: "+ status +"</h3>";
		out += "<h3>Do you want to try again?</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<form action=\"newkey\" method=\"post\">	";
		out += "<button class=\"button radius\" type=\"submit\" name=\"button_start\" value=\"new\">Yes, let's try again</button>		";
		out += "</form>";
		out += "<a href=\"login\">No, just forget about it.</a>";
		out += "</div>";
		out += "</div>";
		
		return out;	}

	
	
	/**
	 * This function performs all actions required to display new key protection password page:
	 * @param 
	 */
	public void routineNewKeysModal(){
		resetGlobals();
		this.mode = "2";
		this.outModal = drawNewKeyPassword();
	}
	
	public String drawNewKeyPassword(){
		String out = "";
		
		out += "<h5>Generate New Signing Key</h5>";
		out += "<form id=\"form_new_keys\" action=\"newkey\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw key protection fields:
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Key Protecdtion Password</legend>";
		out += HtmlService.drawInputTextPasswordAndConfirmation("new_key_password", "Password", "new_key_password_confirm", "Confirm Password");
		// button
		out += "<div class=\"row\"><h5>Once you hit the generate button, we will generate new protected signing key for you and send it to your email address.</h5></div>";
		out += "<div class=\"row\"><h5>P.S. this might take few seconds, please wait.</h5></div>";
		out += "<div class=\"row\"><h5><font color=\"#FF0000\">";
		out += "All your voting in any un-closed elections will not be casted, "
				+ "and you will not be able to re-vote again in those un-closed elections.";
		out += "</font></h5></div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"button_generate\">Generate</button>";
		out += "<a class=\"radius button right\" href=\"login\">Cancel</a>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "</fieldset>";
		out += "</form>";

		
		return out;
	}
	
	private void resetGlobals() {
		this.mode = "1";
		this.messageAlert = "";
		this.messageLabel = "";
		this.outModal = "";
	}
	

	
	
}
