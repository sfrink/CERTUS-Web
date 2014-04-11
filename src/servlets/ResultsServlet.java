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
import service.HtmlService;
import service.VotingService;
import dto.CandidateDto;
import dto.ElectionDto;
import dto.Validator;
import dto.VoteDto;

/**
 * Servlet implementation class TallyingServlet
 */
@WebServlet("/results")
public class ResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outElections = "";
	private String outModal = "";
	
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
			resetGlobals();
			routineExistingElections();
				
			// Redirect
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/results.jsp");
			rd.forward(request, response);			
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		if(HeaderService.isAuthenticated()) {
			resetGlobals();
			
			// OPEN RETULTS
			if(request.getParameter("button_election_results") != null) {
				routineElectionResults(request);
			}
						
			routineExistingElections();

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/results.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}

		
	/**
	 * Dmitriy Karmazin
	 * This function performs all the routines required for displaying existing elections
	 */
	public void routineExistingElections() {
		ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();

		// 1. get the list of all elections this user voted in and which are closed
		Validator vAllElections = ElectionService.selectElectionsForResults(HeaderService.getUserId());

		if(vAllElections.isVerified()) {
			allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
		} else {
			messageAlert = HtmlService.drawMessageAlert(vAllElections.getStatus(), "alert") ;
		}
			
		outElections = drawElectionsTableForResults(allElections);
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs required routine to display election results
	 * @param request
	 */
	public void routineElectionResults(HttpServletRequest request) {
		resetGlobals();
		int electionId = Integer.parseInt(request.getParameter("button_election_results"));
		Validator v = ElectionService.selectResults(electionId);
		
		if(v.isVerified()) {
			// get election object
			ElectionDto e = (ElectionDto) v.getObject();
			this.mode = "2";
			this.outModal = drawElectionResults(e);
		} else {
			this.messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");
		}
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
			out += "<h5>List of elections with published results</h5>";
			out += "<h5><form action=\"results\" method=\"post\">";
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
	
	
	
	public String drawElectionResults(ElectionDto e) {
		String out = "";
		
		if(e == null) {
			return out;			
		}

		out += "<form action=\"voting\" method=\"post\">";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 large-centered columns\">";
		out += "<h2>" + e.getElectionName() + "</h2>";

		out += "<fieldset>";
		out += "<legend>Election results</legend>";
		out += "<div class=\"row\">";
		out += "<h3>" + drawWinnerString(e.getCandidateList()) + "</h3>";
		out += "</div>";
		
		out += "<div class=\"row\">";		
		out += "<table><thead><tr>";
		out += "<th>Candidate Name</th>";
		out += "<th>Number of votes</th>";
		out += "</tr></thead><tbody>";

		for (CandidateDto c : e.getCandidateList()) {
			String outClass = c.isWinner() ? "class_type_bold" : "";
			out += "<tr>";
			out += "<td><span class=\"" + outClass + "\">" + c.getCandidateName() +  "<span></td>";
			out += "<td><span class=\"" + outClass + "\">" + c.getVoteCount() + "<span></td>";			
			out += "</tr>";
		}
		
		out += "</tbody></table>";
	    out += "</div>";
		out += "</fieldset>";
	    out += "</div>";
	    out += "</form>";		
		
		return out;
	}

	
	
	/**
	 * Dmitriy Karmazin
	 * This functions resets all global variables for this class
	 */
	private void resetGlobals() {
		this.mode = "1";
		this.messageAlert = "";
		this.messageLabel = "";
		this.outElections = "";
		this.outModal = "";
	}

	
	/**
	 * This function returns string that declares the winner of election
	 * @param candidates
	 * @return
	 */
	public String drawWinnerString(ArrayList<CandidateDto> candidates) {
		String outWinner1 = "", outWinner2 = "";
		int winnerCount = 0;
		
		for (CandidateDto c : candidates) {
			if(c.isWinner()) {
				winnerCount++;
				outWinner2 += c.getCandidateName() + ", ";
			}
		}

		if(winnerCount == 0) {
			outWinner1 = "No votes had been submitted";
			outWinner2 = "";
		} else if(winnerCount == 1) {
			outWinner1 = "The winner of the election is: ";
			outWinner2 = outWinner2.substring(0, outWinner2.length() - 2);
		} else if(winnerCount > 1) {
			outWinner1 = "There is a tie between: ";
			outWinner2 = outWinner2.substring(0, outWinner2.length() - 2);
		}

		return outWinner1 + outWinner2;
	}
}
