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
import enumeration.UserStatus;
import enumeration.UserType;
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
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
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

		if (HeaderService.isTempUser()){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
			rd.forward(request, response);
		} else if(HeaderService.isAuthenticated()) {
			// logged in, redirect to main
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
			rd.forward(request, response);
		} else if(request.getParameter("getTemp") != null) {
			String email=request.getParameter("username");
			
			Validator v=UserService.selectUserByEmail(email);
			UserDto u=(UserDto)v.getObject();
			
			if(!v.isVerified()){
				String message="The provided email is not registered.";
				outForm=drawEmailFieldsetEmailFailed(null, message);
				request.setAttribute("message_alert",messageAlert);
				request.setAttribute("out_form", outForm);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/forgot.jsp");		
				rd.forward(request, response);
			} else {
				if(u.getStatus()==UserStatus.LOCKED.getCode()){
					String message="The account associated with this email is locked.";
					outForm=drawEmailFieldsetEmailFailed(null, message);
					request.setAttribute("message_alert",messageAlert);
					request.setAttribute("out_form", outForm);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/forgot.jsp");		
					rd.forward(request, response);
				} else {
					if(u.getType()==UserType.ELECTORATE.getCode() || u.getType()==UserType.AUTHORITY.getCode()){
						EditProfileService.sendTempPassword(u).isVerified();
					} else if(u.getType()==UserType.INVITED.getCode()){
						InvitedUserService.resendInvitation(u).isVerified();
					}
					request.setAttribute("email", email);
					request.setAttribute("message_alert",messageAlert);
					request.setAttribute("out_form", outForm);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp");		
					rd.forward(request, response);
				}
			}
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
	
	public String drawEmailFieldsetEmailFailed(UserDto u, String message) {
		String out = "";
		if(u == null) {
			u = new UserDto();
			u.setEmail("");
		}
		out += "<form action=\"forgot\" method=\"post\" data-abide>";
		out += "<fieldset>";
		out += "<legend>Reset Password</legend>";
		out += HtmlService.drawInputTextEmailForgot("username", "Email", "your@email.address", u.getEmail(), message);		
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
