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
import service.LoginService;



/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Validator v = LoginService.authenticate(username, password);
		request.setAttribute("username", username);
		
		if(v.isVerified()) {
			UserDto u = (UserDto) v.getObject();
			request.setAttribute("message", v.getStatus() + ", " + u.getFirstName() + " " + u.getLastName());

			HeaderService.authenticate();
			HeaderService.setUserId(u.getUserId());
			
			request.setAttribute("state", "success" );
			RequestDispatcher rd = request.getRequestDispatcher("main.jsp");
			rd.forward(request, response);
			return;
		} else {
			request.setAttribute("state", "fail" );
			request.setAttribute("message", v.getStatus());
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.forward(request, response);
			return;
		}
	}
}
