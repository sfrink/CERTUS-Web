package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.ElectionDto;
import dto.Validator;
import service.ElectionService;
import service.HeaderService;
import service.HtmlService;

/**
 * Dmitriy Karmazin
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outElectionsForVoting = "";
	private String outElectionsForResults = "";
	private String outModal = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();
		System.out.println("asd");

		if(HeaderService.isAuthenticated()) {

			routineExistingElectionsForVoting();
			routineExistingElectionsForResults();
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections_for_voting", outElectionsForVoting);
			request.setAttribute("out_elections_for_results", outElectionsForResults);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}		
	}
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resetGlobals();
		
		if(HeaderService.isAuthenticated()) {

			routineExistingElectionsForVoting();
			routineExistingElectionsForResults();

			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections_for_voting", outElectionsForVoting);
			request.setAttribute("out_elections_for_results", outElectionsForResults);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}		
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for given list of elections
	 * @param elections
	 * @return
	 */
	public String drawElectionsTableForVoting(ArrayList<ElectionDto> elections) {
		String out = "";

		if(elections != null && elections.size() != 0) {
			out += "<h5>List of elections to vote</h5>";
			out += "<h5><form action=\"voting\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>Election Name</th>";
			out += "<th>Action</th>";
			out += "</tr></thead><tbody>";
				
			for (ElectionDto e : elections) {
				out += "<tr>";
				out += "<td>" + e.getElectionName() + "</td>";
				out += "<td><button class=\"label radius\" type=\"submit\" name=\"button_vote\" value=\"" + e.getElectionId() + "\">vote</button></td>";
				out += "</tr>";
			}
				
			out += "</tbody></table>";
			out += "</form>";
		} else {
			out += "<div class=\"label secondary\">No elections available yet</div>";
		}
			
		return out;
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for table of elections with available results
	 * @param elections
	 * @return
	 */	
	public String drawElectionsTableForResults(ArrayList<ElectionDto> elections) {
		String out = "";

		if(elections != null && elections.size() != 0) {
			out += "<form action=\"results\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>Election Name</th>";
			out += "<th>Action</th>";
			out += "</tr></thead><tbody>";
			
			int i = 1;
			for (ElectionDto e : elections) {
				out += "<tr>";
				out += "<td>" + e.getElectionName() + "</td>";
				out += "<td><button class=\"label radius\" type=\"submit\" name=\"button_election_results\" value=\"" + e.getElectionId() + "\">See results</button></td>";
				out += "</tr>";
				i++;
			}
			
			out += "</tbody></table>";
			out += "</form>";
		} else {
			out += "<div class=\"label secondary\">No election results available yet</div>";
		}
		
		return out;
	}

	
	
	
	
	
	
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs all routines required to get and display all elections this user can vote in
	 */
	public void routineExistingElectionsForVoting() {
		// 1. get the list of all elections this user can vote in
		ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();
		Validator vAllElections = ElectionService.selectElectionsForVoter(HeaderService.getUserId());
		if(vAllElections.isVerified()) {
			allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
		} else {
			messageAlert = HtmlService.drawMessageAlert(vAllElections.getStatus(), "alert") ;
		}
		// 2. draw output
		outElectionsForVoting = drawElectionsTableForVoting(allElections);
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function performs all the routines required for displaying existing elections
	 */
	public void routineExistingElectionsForResults() {
		ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();

		// 1. get the list of all elections this user voted in and which are closed
		Validator vAllElections = ElectionService.selectElectionsForResults(HeaderService.getUserId());

		if(vAllElections.isVerified()) {
			allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
		} else {
			messageAlert = HtmlService.drawMessageAlert(vAllElections.getStatus(), "alert") ;
		}
			
		outElectionsForResults = drawElectionsTableForResults(allElections);
	}

	
	
	
	
	/**
	 * Dmitriy Karmazin
	 * This functions resets all global variables for this class
	 */
	private void resetGlobals() {
		this.mode = "1";
		this.messageAlert = "";
		this.messageLabel = "";
		this.outElectionsForVoting = "";
		this.outElectionsForResults = "";
		this.outModal = "";
	}
}
