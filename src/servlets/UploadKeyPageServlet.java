package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.HeaderService;
import service.HtmlService;

/**
 * Servlet implementation class UploadKeyPageServlet
 */
@WebServlet("/uploadkeypage")
public class UploadKeyPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadKeyPageServlet() {
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
		} else if(!HeaderService.isAuthenticated(request)) {
			// not logged in, redirect to login
			messageAlert = HtmlService.drawMessageAlert("Select option to proceed", "");
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");		
			rd.forward(request, response);
		} else {
			resetGlobals();
			routineUploadKeyModal();
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			request.setAttribute("message_alert", messageAlert);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/uploadKey.jsp");		
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(HeaderService.isAuthenticated(request)) {
			
			resetGlobals();
			routineUploadKeyModal();
			
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_modal", outModal);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/uploadKey.jsp");
			rd.forward(request, response);
		}else{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}
	
	
	private void resetGlobals() {
		this.messageAlert = "";
		this.mode = "1";
		messageLabel = "";
		outModal = "";
	}

	
	private void routineUploadKeyModal(){
		resetGlobals();
		outModal = drawMainPage();
	}
	
	
	private String drawMainPage(){
		String out = "";
		
		
		out += "<h5>";
		out += "<font color=\"red\">Warning:</font> If you generate a new signing key, All your voting in any un-closed elections will not be casted, ";
		out += "and you will not be able to re-vote again in those un-closed elections.";
		out += "</h5>";
		
		out += "<form action=\"uploadkey\" method=\"post\" enctype=\"multipart/form-data\" onsubmit=\"return showFileSize()\" data-abide>";
		out += "<div class=\"row\">";
			out += "<div class=\"large-6 medium-6 columns\">";
		
				out += "<fieldset>";
					out += "<legend>Your Public Key path</legend>";
					out += HtmlService.drawInputTextPassword("user_password", "Your Password", "Your Password", "", false, "");			
					//error messages:
					out += "<div id=\"emptyFileError\" style=\"display: none\"><p><font color=\"red\">Please select a file.</font></p></div>";
					out += "<div id=\"largeFileError\" style=\"display: none\"><p><font color=\"red\">The file is larger than 10 kilobytes.</font></p></div>";
					out += "<div id=\"apiFileError\" style=\"display: none\"><p><font color=\"red\">The file API isn't supported on this browser yet.</font></p></div>";
					out += "Your file size cannot be larger than 10 bytes.";
					out += "<div class=\"row\">";						
						out += "<input name=\"uploadFile\" id=\"FileInput\" type=\"file\" size=\"10240\">";
						out += "<button id=\"button_start_uploading\" type=\"submit\" value=\"Upload\" class=\"radius button right\" name=\"upload_and_submit_button\">Upload</button>";
					out += "</div>";
				out += "</fieldset>";
			out += "</div>";		
		out += "</div>";
		out += "</form>";

		return out;
	}

}
