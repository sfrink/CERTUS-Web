package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ElectionService;
import service.HeaderService;
import dto.ElectionDto;
import dto.Validator;

/**
 * Servlet implementation class TallyingServlet
 */
@WebServlet("/results")
public class ResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String messageAlert = "";
	private String messageLabel = "";

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResultsServlet() {
        super();
    }


    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(HeaderService.isAuthenticated()) {
			messageAlert = "";
			messageLabel = "";

			
			
			// get list of all elections user is eligible to vote in, which are closed
			// 
			
			
			
			
			
			
			
			
			
			
			
			
			// Redirect
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);

			RequestDispatcher rd = getServletContext().getRequestDispatcher("/results.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
			rd.forward(request, response);
		}

		
		
	}

}
