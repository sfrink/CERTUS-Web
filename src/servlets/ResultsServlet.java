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
			mode = "1";
			messageAlert = "";
			messageLabel = "";
			outModal = "";

			ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();

			// 1. get the list of all elections this user voted in and which are closed
			Validator vAllElections = ElectionService.selectElectionsForResultsForUser(HeaderService.getUserId());

			if(vAllElections.isVerified()) {
				allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
			} else {
//				messageAlert = drawMessageAlert(vAllElections.getStatus(), "alert") ;
			}
				
			outElections = drawElectionsTableForResults(allElections);
				
				
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
			mode = "1";
			messageAlert = "";
			messageLabel = "";
			outModal = "";

			ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();


			// OPEN RETULTS
			if(request.getParameter("button_election_results") != null) {
				int electionId = Integer.parseInt(request.getParameter("button_election_results"));
				Validator v = ElectionService.selectResults(electionId);
				
				if(v.isVerified()) {
					// get election object
					ElectionDto e = (ElectionDto) v.getObject();
					
					
					outModal = drawElectionResults(e);
					mode = "2";
				}
			}
						
			// 1. get the list of all elections this user can vote in
			Validator vAllElections = ElectionService.selectElectionsForResultsForUser(HeaderService.getUserId());

			if(vAllElections.isVerified()) {
				allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
			} else {
				messageAlert = vAllElections.getStatus();
			}
			
			outElections = drawElectionsTableForResults(allElections);


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

	
	
	
	
	
	public String drawElectionsTableForResults(ArrayList<ElectionDto> elections) {
		String out = "";

		if(elections != null && elections.size() != 0) {
			out += "<h5>List of finished elections</h5>";
			out += "<h5><form action=\"results\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>ID</th>";
			out += "<th>Election Name</th>";
			out += "<th>Action</th>";
			out += "</tr></thead><tbody>";
			
			int i = 1;
			for (ElectionDto e : elections) {
				out += "<tr>";
				out += "<td>" + e.getElectionId() + "</td>";
				out += "<td>" + e.getElectionName() + "</td>";
				out += "<td><button class=\"label success\" type=\"submit\" name=\"button_election_results\" value=\"" + e.getElectionId() + "\">See results</button></td>";
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
		
		out += "<table><thead><tr>";
		out += "<th>Candidate Name</th>";
		out += "<th>Number of votes</th>";
		out += "</tr></thead><tbody>";

		for (CandidateDto c : e.getCandidateList()) {
			out += "<tr>";
			out += "<td>" + c.getCandidateName() + "</td>";
			out += "<td>" + c.getVoteCount() + "</td>";			
			out += "</tr>";
		}
		
		out += "</tbody></table>";
	    out += "</div>";
		
		out += "</fieldset>";

	    out += "</div>";
	    out += "</form>";		
		
		return out;
	}

	
	
	
	
	
	
	
	
}