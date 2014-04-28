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
import service.LoginService;
import service.UserService;

/**
 * Servlet implementation class LoginWithTempServlet
 */
@WebServlet("/loginWithTemp")
public class LoginWithTempServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String messageAlert = "";   
	private String outForm = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginWithTempServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();
		if (HeaderService.isTempUser(request)){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
			rd.forward(request, response);
		} else if(HeaderService.isAuthenticated(request)) {
			// logged in, redirect to main
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
			rd.forward(request, response);
			
		} else if(request.getAttribute("messageAlert").equals("1")){
			UserDto u=new UserDto();
			outForm=drawLoginFieldset(u);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp");	
			rd.forward(request, response);
		}
		else if(request.getAttribute("messageAlert").equals("2")){
			outForm=resentInvitation();
			request.setAttribute("message_alert",messageAlert);
			request.setAttribute("out_form",outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp");	
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (HeaderService.isTempUser(request)){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
			rd.forward(request, response);
		} else if(HeaderService.isAuthenticated(request)) {
			// logged in, redirect to main
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
			rd.forward(request, response);
		} else if(request.getParameter("login") != null) {
			// login button clicked
			UserDto u = new UserDto();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String newPassword=request.getParameter("new_password");
			Validator v = LoginService.authenticateTemp(username, password, newPassword);
				
			if(v.isVerified()) {
				u = (UserDto) v.getObject();
				HeaderService.authenticate(request);
				HeaderService.setUserId(request, u.getUserId());
				HeaderService.setUserSessionId(request, u.getSessionId());
				HeaderService.setUserName(request, username);
				HeaderService.setUserType(request, u.getType());
				HeaderService.setLoginWithTemp(request, true);
				request.setAttribute("message_alert", messageAlert);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
				rd.forward(request, response);
			} else {
				u.setEmail(username);
				u.setPassword(password);
				outForm = drawLoginFieldset(u);
				messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");
				request.setAttribute("message_alert", messageAlert);
				request.setAttribute("out_form", outForm);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp.jsp");		
				rd.forward(request, response);
			}
		}
		else{
			String email=(String)request.getAttribute("email");
			Validator val=UserService.selectUserByEmail(request, email);
			UserDto user=new UserDto();
			if(val.isVerified()){
				user=(UserDto)val.getObject();
				if(user.getType()==0){
					UserDto u=new UserDto();
					outForm=drawLoginFieldset(u);
					request.setAttribute("message_alert", messageAlert);
					request.setAttribute("out_form", outForm);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp.jsp");	
					rd.forward(request, response);
				}
				else if(user.getType()==2){
					outForm=resentInvitation();
					request.setAttribute("message_alert",messageAlert);
					request.setAttribute("out_form",outForm);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp.jsp");	
					rd.forward(request, response);
				}
			}
			else{
				System.out.println("Something wrong with email; do something here");
			}
		}
	}
	
	public String drawLoginFieldset(UserDto u) {
		String out = "";
		if(u == null) {
			u = new UserDto();
			u.setEmail("");
			u.setPassword("");
		}
		
		out += "<form action=\"loginWithTemp\" method=\"post\" data-abide>";
		out += "<fieldset>";
		out += "<legend>Use your email and temporary password to log in</legend>";
		out += HtmlService.drawInputTextEmail("username", "Email", "your@email.address", "");		
		out += HtmlService.drawInputTextPassword("password", "Temporary Password", "password", "", false, "");
		out += HtmlService.drawInputTextPasswordAndConfirmation("new_password", "New Password", "new_password_confirm", "Confirm New Password", false);
		out += "<input type=\"submit\" name=\"login\" class=\"small radius button\" value=\"Login\">";
		out += "</fieldset>";
		out += "</form>";
		
		return out;
	}
	
	public String resentInvitation(){
		String out="";
		out+="<div>";
		out+="<p>Your email address is already associated with an account.\n</p>";
		out+="<p>You must login with your email address and complete registration.\n</p>";
		out+="<p>We have resent an invitation to your email address.</p>";
		return out;	
	}

	private void resetGlobals() {
		this.messageAlert = "";
		this.outForm = "";
	}
}
