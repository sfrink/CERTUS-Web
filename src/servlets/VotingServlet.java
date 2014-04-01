package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.HtmlService;
import service.ElectionService;
import service.HeaderService;
import service.VotingService;
import dto.CandidateDto;
import dto.ElectionDto;
import dto.Validator;
import dto.VoteDto;

/**
 * Servlet implementation class VotingServlet
 */
@WebServlet("/voting")
public class VotingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outElections = "";
	private String outModal = "";

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VotingServlet() {
        super();
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(HeaderService.isAuthenticated()) {
			resetGlobals();
			routineExistingElectionsToVote();
				
			// Redirect
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/voting.jsp");
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

			if(request.getParameter("button_vote") != null) {
				// VOTE STEP 1
				routineVoteModalStep1(request);
			} else if(request.getParameter("button_next") != null) {
				// VOTE STEP 2
				routineVoteModalStep2(request);
			} else if (request.getParameter("button_submit_vote") != null) {
				// VOTE SUBMITTED
				routineVoteSubmittedRoutine(request);
			}

			// update the list of all elections
			routineExistingElectionsToVote();

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/voting.jsp");
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
	 * This function performs all routines required to get and display all elections this user can vote in
	 */
	public void routineExistingElectionsToVote() {
		// 1. get the list of all elections this user can vote in
		ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();
		Validator vAllElections = ElectionService.selectAllElectionsForVoter(HeaderService.getUserId());
		if(vAllElections.isVerified()) {
			allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
		} else {
			messageAlert = HtmlService.drawMessageAlert(vAllElections.getStatus(), "alert") ;
		}
		// 2. draw output
		outElections = drawElectionsTableForVoting(allElections);
	}
	
		
	/**
	 * Dmitriy Karmazin
	 * This function performs all actions required to display step 1 of voting
	 * @param request
	 */
	public void routineVoteModalStep1(HttpServletRequest request) {
		resetGlobals();
		int electionId = Integer.parseInt(request.getParameter("button_vote"));
		Validator v = ElectionService.selectElection(electionId);
		
		if(v.isVerified()) {
			// get election object
			ElectionDto e = (ElectionDto) v.getObject();

			this.mode = "2";
			this.outModal = drawVotingInterfaceForElectionStep1(e);
		} else {
			this.messageLabel = HtmlService.drawMessageLabel(v.getStatus(), "alert");
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
		Validator v = ElectionService.selectElection(electionId);
		
		if(v.isVerified()) {
			// extract election object
			ElectionDto e = (ElectionDto) v.getObject();

			// get selected option
			if(request.getParameter("voting_choice") != null) {
				int candidateId = Integer.parseInt(request.getParameter("voting_choice"));
				Validator vEnc = VotingService.encryptCandidateId(candidateId);

				if(vEnc.isVerified()) {
					String cipherText = (String) vEnc.getObject();
					
					VoteDto vote = new VoteDto();
					vote.setUserId(HeaderService.getUserId());
					vote.setElectionId(electionId);
					vote.setVoteEncrypted(cipherText);

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
			messageLabel = HtmlService.drawMessageLabel(v.getStatus(), "alert");
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
			Validator v = ElectionService.selectElection(electionId);

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
					outModal = drawVotingInterfaceForElectionStep2(e, vote);
				}
			} else {
				messageLabel = HtmlService.drawMessageLabel(v.getStatus(), "alert");
			}
		} else {
			messageLabel = HtmlService.drawMessageLabel("Signature cannot be empty", "alert");
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
		this.outElections = "";
		this.outModal = "";
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
		out += "<form action=\"voting\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Step 1: Please select candidate</legend>";

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
		out += "<fieldset>";
		out += "<legend>Election Description</legend>";
		out += e.getElectionDescription();
		out += "</fieldset>";
		out += "</div>";
		out += "</div>";
	    out += "</form>";		
		
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
		out += "<form id=\"form_vote_step2\" action=\"voting\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		//	Cipher and signature
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Step 2: Please sign your vote</legend>";
		out += HtmlService.drawInputTextareaReadonly("text_cipher", "Encrypted Vote", "Cipher is supposed to be here", vote.getVoteEncrypted());
		out += HtmlService.drawInputTextareaAlphanumeric("text_signature", "Signature", "Enter your signature here...", signature);
		out += "</fieldset>";
		// buttons
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 large-centered columns\">";
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
//		out += "<legend>Thank you!</legend>";
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
		
		return out;
	}

}
	
	

	

