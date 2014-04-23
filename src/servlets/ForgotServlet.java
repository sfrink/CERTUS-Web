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
import service.HtmlService;
import service.HeaderService;
import service.InvitedUserService;
import service.LoginService;
import service.UserService;



/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/forgot")
public class ForgotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String messageAlert = "";   
	private String outForm = "";

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgotServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();
		if (HeaderService.isTempUser()){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
			rd.forward(request, response);
		} else if(HeaderService.isAuthenticated()) {
			// logged in, redirect to main
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
		} else {
			outForm=drawEmailFieldset(null);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/forgot.jsp");	
			rd.forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();

		if(request.getParameter("getTemp") != null) {
			String email=request.getParameter("username");
			
			Validator v=UserService.selectUserByEmail(email);
			UserDto u=(UserDto)v.getObject();
			//check if regular user
			if(u.getType()==0){
				EditProfileService.sendTempPassword(u);
			}
			else if(u.getType()==2){
				InvitedUserService.resendInvitation(u);
			}
			request.setAttribute("email", email);
			request.setAttribute("message_alert",messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp");		
			rd.forward(request, response);
		}
	}
	
	public String drawEmailFieldset(UserDto u) {
		String out = "";
		if(u == null) {
			u = new UserDto();
			u.setEmail("");
		}
		
		out += "<form action=\"forgot\" method=\"post\" data-abide>";
		out += "<fieldset>";
		out += "<legend>Reset Password</legend>";
		out += HtmlService.drawInputTextEmail("username", "Email", "your@email.address", u.getEmail());		
		out += "<input type=\"submit\" name=\"getTemp\" class=\"small radius button\" value=\"Send Email\">";
		out += "</fieldset>";
		out += "</form>";
		
		return out;
	}
	
	private void resetGlobals() {
		this.messageAlert = "";
		this.outForm = "";
	}

}
