package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.CandidateDto;
import dto.ElectionDto;
import dto.Validator;
import dto.VoteDto;
import service.ElectionService;
import service.HeaderService;
import service.HtmlService;
import service.VotingService;

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

			// OPEN RETULTS
			if(request.getParameter("button_election_results") != null) {
				routineElectionResults(request);
			} else if(request.getParameter("button_vote") != null) {
				// VOTE STEP 1
				routineVoteModalStep1(request);
			} else if(request.getParameter("button_next") != null) {
				// VOTE STEP 2
				routineVoteModalStep2(request);
			} else if (request.getParameter("button_submit_vote") != null) {
				// VOTE SUBMITTED
				routineVoteSubmittedRoutine(request);
			}

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
			out += "<h5><form action=\"main\" method=\"post\">";
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
			out += "<form action=\"main\" method=\"post\">";
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
	 * This function returns HTML modal for election results
	 * @param e
	 * @return
	 */
	public String drawElectionResults(ElectionDto e) {
		String out = "";
		
		if(e == null) {
			return out;			
		}

		out += "<form action=\"voting\" method=\"post\">";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 large-centered columns\">";
		out += "<h3>Election Results: " + e.getElectionName() + "</h3>";
		out += "<h4>" + drawWinnerString(e.getCandidateList()) + "</h4>";
		
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
	    out += "</div>";
	    out += "</form>";
	    out += "<a id=\"close-reveal-modal-modified-force\" class=\"close-reveal-modal\">&#215;</a>";
		
		return out;
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
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for voting step 1
	 * @param e
	 * @return
	 */
	public String drawVotingInterfaceForElectionStep1(ElectionDto e) {
		String out = "", selected;
		
		if(e == null) {
			return out;			
		}

		out += "<h2>" + e.getElectionName() + "</h2>";
		out += "<form action=\"main\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Step 1 of 3: Please select candidate</legend>";

		for (CandidateDto c : e.getCandidateList()) {
			out += "<label for=\"" + c.getCandidateId() + "\">";			
			out += "<input type=\"radio\" id=\"" + c.getCandidateId() + "\" name=\"voting_choice\" value=\"" + c.getCandidateId() + "\" required>";
			out += " " + c.getCandidateName() + "</label>";
		}		
		out += "</fieldset>";
		out += "<button class=\"button\" type=\"submit\" name=\"button_next\" value=\"" + e.getElectionId() + "\">Next</button>";
		out += "</div>";
		// draw election description
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Election Close Time</legend>";
		out += (e.getStartDatetime() == null) ? "No closing time specified" : e.getStartDatetime();
		out += "</fieldset>";
		out += "<fieldset><legend>Election Description</legend>";
		out += e.getElectionDescription();
		out += "</fieldset>";
		out += "</div>";
		out += "</div>";
	    out += "</form>";		
	    out += "<a id=\"close-reveal-modal-modified_election\" class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for step 2 of voting
	 * @param e
	 * @param vote
	 * @return
	 */
	public String drawVotingInterfaceForElectionStep2(ElectionDto e, VoteDto vote) {
		String out = "";
		
		if(e == null || vote == null) {
			return out;			
		}

		String signature = (vote.getVoteSignature() == null) ? "" : vote.getVoteSignature();

		out += "<h2>" + e.getElectionName() + "</h2>";
		out += "<form id=\"form_vote_step2\" action=\"main\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		//	Cipher and signature
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Step 2 of 3: Please sign your vote</legend>";
		out += HtmlService.drawInputTextareaReadonly("text_cipher", "Encrypted Vote", "Cipher is supposed to be here", vote.getVoteEncrypted());
		out += HtmlService.drawInputTextareaAlphanumeric("text_signature", "Signature", "Enter your signature here...", signature, vote.isVoteSignatureError(), vote.getVoteSignatureErrorMessage(), true);
		out += "</fieldset>";
		// buttons
		out += "<div class=\"row\">";
		out += "<div class=\"large-12 medium-12 small-12 large-centered medium-centered small-centered columns\">";
		out += "<button id=\"button_vote_back\" class=\"button alert left\" type=\"button\" name=\"button_vote\" value=\"" + vote.getElectionId() + "\">Back</button>";
		out += "<button class=\"button success right\" type=\"submit\" name=\"button_submit_vote\" value=\"" + vote.getElectionId() + "\">Vote!</button>";	
		out += "</div>";
		out += "</div>";
		out += "</div>";

		// Election description or instructions
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Election Description</legend>";
		out += e.getElectionDescription();
		out += "</fieldset>";
		out += "</div>";
		out += "</div>";
	    out += "</form>";
	    out += "<a id=\"close-reveal-modal-modified_election\" class=\"close-reveal-modal\">&#215;</a>";
		
		return out;
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function draws HTML output for the screen to be shown in case of successful voting
	 */
	public String drawVotingInterfaceForElectionStep3() {
		String out = "";
		
		out += "<h2>Thank You!</h2>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 large-centered columns\">";
		out += "<fieldset>";
		out += "<legend>Step 3 of 3: Confirmation</legend>";
		out += "Your vote has been successfully submitted!";
		out += "</fieldset>";
		out += "</div>";
		out += "</div>";
		// button
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 large-centered columns\">";
		out += "<button id=\"button_vote_complete\" class=\"button alert\" type=\"button\"\">Close</button>";		
		out += "</div>";
		out += "</div>";
	    out += "<a id=\"close-reveal-modal-modified-force\" class=\"close-reveal-modal\">&#215;</a>";
		
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
	 * This function performs all actions required to display step 1 of voting
	 * @param request
	 */
	public void routineVoteModalStep1(HttpServletRequest request) {
		resetGlobals();
		int electionId = Integer.parseInt(request.getParameter("button_vote"));
		Validator v = ElectionService.selectElectionForVoter(electionId);
		
		if(v.isVerified()) {
			// get election object
			ElectionDto e = (ElectionDto) v.getObject();

			this.mode = "2";
			this.outModal = drawVotingInterfaceForElectionStep1(e);
		} else {
			this.messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs routine required to process choise of candidate and ecrypt it
	 * @param request
	 */
	public void routineVoteModalStep2(HttpServletRequest request) {
		resetGlobals();
		int electionId = Integer.parseInt(request.getParameter("button_next"));
		Validator v = ElectionService.selectElectionForVoter(electionId);
		
		if(v.isVerified()) {
			// extract election object
			ElectionDto e = (ElectionDto) v.getObject();

			// get selected option
			if(request.getParameter("voting_choice") != null) {
				int candidateId = Integer.parseInt(request.getParameter("voting_choice"));
				Validator vEnc = VotingService.encryptCandidateId(candidateId, electionId);

				if(vEnc.isVerified()) {
					String cipherText = (String) vEnc.getObject();
					
					VoteDto vote = new VoteDto();
					vote.setUserId(HeaderService.getUserId());
					vote.setElectionId(electionId);
					vote.setVoteEncrypted(cipherText);
					vote.setVoteSignatureError(false);
					vote.setVoteSignatureErrorMessage("");

					mode = "2";
					outModal = drawVotingInterfaceForElectionStep2(e, vote);
				} else {
					mode = "2";					
					messageLabel = HtmlService.drawMessageLabel(vEnc.getStatus(), "alert");
					outModal = drawVotingInterfaceForElectionStep1(e);
				}
			} else {
				mode = "2";
				outModal = drawVotingInterfaceForElectionStep1(e);
				messageLabel = HtmlService.drawMessageLabel("Please select candidate", "alert");
			}
		} else {
			messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs routine required in order to process submitted vote
	 * @param request
	 */
	public void routineVoteSubmittedRoutine(HttpServletRequest request) {
		resetGlobals();

		if (request.getParameter("text_cipher") != null && request.getParameter("text_signature") != null) {
			// get election
			int electionId = Integer.parseInt(request.getParameter("button_submit_vote"));
			Validator v = ElectionService.selectElectionForVoter(electionId);

			if (v.isVerified()) {
				// get election object
				ElectionDto e = (ElectionDto) v.getObject();
				String inputCipher = request.getParameter("text_cipher");
				String inputSignature = request.getParameter("text_signature");

				VoteDto vote = new VoteDto();
				vote.setUserId(HeaderService.getUserId());
				vote.setElectionId(electionId);
				vote.setVoteEncrypted(inputCipher);
				vote.setVoteSignature(inputSignature);

				Validator vVote = VotingService.saveVote(vote);
				if (vVote.isVerified()) {
					mode = "2";
					outModal = drawVotingInterfaceForElectionStep3();
					//messageLabel = HtmlService.drawMessageAlert(vVote.getStatus(), "success");					
				} else {
					mode = "2";
					messageLabel = HtmlService.drawMessageLabel(vVote.getStatus(), "alert");
					outModal = drawVotingInterfaceForElectionStep2(e, (VoteDto) vVote.getObject());
				}
			} else {
				messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert");
			}
		} else {
			messageAlert = HtmlService.drawMessageAlert("Signature cannot be empty", "alert");
		}
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
