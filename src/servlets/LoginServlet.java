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
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if(HeaderService.isAuthenticated()) {
			messageAlert = HtmlService.drawMessageAlert("Select option to proceed", "");
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");		
			rd.forward(request, response);
		} else {
			messageAlert = HtmlService.drawMessageAlert("Welcome to CERTUS! Please login or register before using the system", "");
			request.setAttribute("message_alert", messageAlert);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		if(request.getParameter("username") == null || request.getParameter("password") == null) {
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.forward(request, response);
		} else {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
		
			Validator v = LoginService.authenticate(username, password);
			request.setAttribute("username", username);
			
			if(v.isVerified()) {
				UserDto u = (UserDto) v.getObject();
				HeaderService.authenticate();
				HeaderService.setUserId(u.getUserId());
	
				messageAlert = HtmlService.drawMessageAlert(v.getStatus() + ", " + u.getFirstName() + " " + u.getLastName() + " uid: " + u.getUserId(), "success");
				request.setAttribute("message_alert", messageAlert);
				RequestDispatcher rd = request.getRequestDispatcher("main.jsp");
				rd.forward(request, response);
			} else {
				messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");
				request.setAttribute("message_alert", messageAlert);
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.forward(request, response);
			}
		}
	}
}
