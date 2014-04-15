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
		
		if(HeaderService.isAuthenticated()) {
			// logged in, redirect to main
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
			
		} else {
			System.out.println("doGet");
			UserDto u=new UserDto();
			outForm=drawLoginFieldset(u);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp.jsp");	
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("login") != null) {
			// login button clicked
			UserDto u = new UserDto();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			Validator v = LoginService.authenticateTemp(username, password);
				
			if(v.isVerified()) {
				u = (UserDto) v.getObject();
				HeaderService.authenticate();
				HeaderService.setUserId(u.getUserId());
				HeaderService.setUserSessionId(u.getSessionId());
				HeaderService.setUserName(username);
				HeaderService.setUserType(u.getType());
				
				request.setAttribute("message_alert", messageAlert);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
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
			outForm=drawLoginFieldset(null);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("out_form", outForm);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginWithTemp.jsp");	
			rd.forward(request, response);		}
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
		out += HtmlService.drawInputTextEmail("username", "Email", "your@email.address", u.getEmail());		
		out += HtmlService.drawInputTextPassword("password", "Password", "password", u.getPassword(), false, "");
		out += "<input type=\"submit\" name=\"login\" class=\"small radius button\" value=\"Login\">";

		out += "</fieldset>";
		out += "</form>";
		
		return out;
	}

	private void resetGlobals() {
		this.messageAlert = "";
		this.outForm = "";
	}
}
