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
import enumeration.UserType;
import service.HtmlService;
import service.HeaderService;
import service.LoginService;



/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String messageAlert = "";   
	private String outForm = "";

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * Dmitriy Karmazin
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();
		
		if(HeaderService.isAuthenticated()) {
			// logged in, redirect to main
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
			rd.forward(request, response);
		} else {
			// user came for the first time, prepare login screen
			routinePrepareFirstLogin();
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
			rd.forward(request, response);
		}
	}

	/**
	 * Dmitriy Karmazin
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();
		
		if(request.getParameter("logout") != null) {
			// logout button clicked
			routineLogout();
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
			rd.forward(request, response);
		} else if(request.getParameter("login") != null) {
			// login button clicked
			UserDto u = new UserDto();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			Validator v = LoginService.authenticate(username, password);
				
			if(v.isVerified()) {
				u = (UserDto) v.getObject();
				
				if (u.getType() == UserType.INVITED.getCode()) {
					// Invited user who does not have updated profile yet.
					HeaderService.deAuthenticate();
					HeaderService.setUserId(u.getUserId());
					HeaderService.setUserType(u.getType());
					HeaderService.setTempUser(true);
					HeaderService.setUserEmail(username);
					HeaderService.setUserSessionId(u.getSessionId());
					
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
					rd.forward(request, response);
				} else {
					HeaderService.authenticate();
					HeaderService.setUserId(u.getUserId());
					HeaderService.setUserSessionId(u.getSessionId());
					HeaderService.setUserName(username);
					HeaderService.setUserType(u.getType());
					HeaderService.setTempUser(false);
					
					request.setAttribute("message_alert", messageAlert);
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/main");		
					rd.forward(request, response);
				}
			} else {
				u.setEmail(username);
				u.setPassword(password);
				outForm = drawLoginFieldset(u);
				messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");

				request.setAttribute("message_alert", messageAlert);
				request.setAttribute("out_form", outForm);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
				rd.forward(request, response);
			}
		} else {
			// something else, perhaps malicious, redirect to login
			routinePrepareFirstLogin();
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
			rd.forward(request, response);
		}		
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function performs all actions required for preparing first login screen
	 */
	public void routinePrepareFirstLogin() {
		resetGlobals();
		outForm = drawLoginFieldset(null);
	}


//	public void routineLogin(HttpServletRequest request, HttpServletResponse response) {
//	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs all actions required for logout procedure
	 */
	public void routineLogout() {
		HeaderService.logout();
		resetGlobals();
		messageAlert = HtmlService.drawMessageAlert("Successfully logged out, thank you for using the system", "");
		outForm = drawLoginFieldset(null);
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for login form
	 * @param UserDto user
	 * @return HTML string
	 */
	public String drawLoginFieldset(UserDto u) {
		String out = "";
		if(u == null) {
			u = new UserDto();
			u.setEmail("dkarmazi@gwu.edu");
			u.setPassword("password");
		}
		
		out += "<form action=\"login\" method=\"post\" data-abide>";
		out += "<fieldset>";
		out += "<legend>Log in to Certus</legend>";
		out += HtmlService.drawInputTextEmail("username", "Email", "your@email.address", u.getEmail());		
		out += HtmlService.drawInputTextPassword("password", "Password", "password", u.getPassword(), false, "");
		out += "<input type=\"submit\" name=\"login\" class=\"small radius button\" value=\"Login\">";
		out += "<p><a href=\"signup\">New User?</a> | <a href=\"forgot\">Forgot your password?</a></p>";
		out += "</fieldset>";
		out += "</form>";
		
		return out;
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This functions resets all global variables for this class
	 */
	private void resetGlobals() {
		this.messageAlert = "";
		this.outForm = "";
	}

	
	
}
