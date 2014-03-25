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
import service.VotingService;

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
        // TODO Auto-generated constructor stub
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
			
			// 1. get the list of all elections this user can vote in
			Validator vAllElections = ElectionService.selectAllElectionsForVoter(HeaderService.getUserId());
			if(vAllElections.isVerified()) {
				allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
			} else {
				messageAlert = drawMessageAlert(vAllElections.getStatus(), "alert") ;
			}
			
			outElections = drawElectionsTableForVoting(allElections);
			
			
			// Redirect
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/voting.jsp");
			rd.forward(request, response);			
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
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


			// OPEN STEP 1
			if(request.getParameter("button_vote") != null) {
				int electionId = Integer.parseInt(request.getParameter("button_vote"));
				Validator v = ElectionService.selectElection(electionId);
				
				if(v.isVerified()) {
					// get election object
					ElectionDto e = (ElectionDto) v.getObject();
					
					outModal = drawVotingInterfaceForElection(e, null);
					mode = "2";
				}
			}
			
			// OPEN STEP 2
			if(request.getParameter("button_next") != null) {
				int electionId = Integer.parseInt(request.getParameter("button_next"));
				Validator v = ElectionService.selectElection(electionId);
				
				if(v.isVerified()) {
					// get election object
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
							
							outModal = drawVotingInterfaceForElection(e, vote);
							mode = "2";
						} else {
							mode = "2";					
							messageLabel = drawMessageLabel(vEnc.getStatus(), "alert");
							outModal = drawVotingInterfaceForElection(e, null);
						}
					} else {
						mode = "2";
						outModal = drawVotingInterfaceForElection(e, null);
						messageLabel = drawMessageLabel("Please select candidate", "alert");
					}
					
				} else {
					messageLabel = drawMessageLabel(v.getStatus(), "alert");
				}
			} else {
				messageLabel = drawMessageLabel("Click next to proceed", "secondary");
			}

			
			// VOTE CLICKED
			if (request.getParameter("button_submit_vote") != null) {
				if (request.getParameter("text_cipher") != null
						&& request.getParameter("text_signature") != null) {

					int electionId = Integer.parseInt(request
							.getParameter("button_submit_vote"));

					Validator v = ElectionService.selectElection(electionId);

					if (v.isVerified()) {
						// get election object
						ElectionDto e = (ElectionDto) v.getObject();

						String inputCipher = request
								.getParameter("text_cipher");
						String inputSignature = request
								.getParameter("text_signature");

						VoteDto vote = new VoteDto();
						vote.setUserId(HeaderService.getUserId());
						vote.setElectionId(electionId);
						vote.setVoteEncrypted(inputCipher);
						vote.setVoteSignature(inputSignature);

						Validator vVote = VotingService.saveVote(vote);
						if (vVote.isVerified()) {
							mode = "1";
							messageAlert = drawMessageAlert(vVote.getStatus(),
									"success");
						} else {
							mode = "2";
							messageLabel = drawMessageLabel(vVote.getStatus(),
									"alert");
							outModal = drawVotingInterfaceForElection(e, vote);
						}
					} else {
						messageLabel = drawMessageLabel(v.getStatus(), "alert");
					}
				} else {
					messageLabel = drawMessageLabel(
							"Signature cannot be empty", "alert");
				}
			}
			
			
			// 1. get the list of all elections this user can vote in
			Validator vAllElections = ElectionService.selectElections();
			if(vAllElections.isVerified()) {
				allElections = (ArrayList<ElectionDto>) vAllElections.getObject();
			} else {
				messageAlert = vAllElections.getStatus();
			}
			
			outElections = drawElectionsTableForVoting(allElections);


			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/voting.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
			rd.forward(request, response);
		}
	}

	
	
	
	
	
	public String drawVotingInterfaceForElection(ElectionDto e, VoteDto vote) {
		String out = "", f1Disabled = "", outStepTwo = "", selected;
		
		if(e == null) {
			return out;			
		}

		out += "<form action=\"voting\" method=\"post\">";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 large-centered columns\">";
		out += "<h2>" + e.getElectionName() + "</h2>";

		if(vote != null) {
			f1Disabled = "disabled";
			outStepTwo = drawVotingInterfaceForElectionStep2(vote);
		    out += outStepTwo;
		} else {
			out += "<fieldset " + f1Disabled + ">";
			out += "<legend>Step 1: Please select candidate</legend>";
			out += "<div class=\"row\">";

			for (CandidateDto c : e.getCandidateList()) {	    	
				out += "<div class=\"row\">";
				out += "<input type=\"radio\" id=\"" + c.getCandidateId() + "\" name=\"voting_choice\" value=\"" + c.getCandidateId() + "\"><label for=\"" + c.getCandidateId() + "\">" + c.getCandidateName() + "</label>";
				out += "</div>";
			}
		
			out += "</fieldset>";
			out += "<button class=\"button\" type=\"submit\" name=\"button_next\" value=\"" + e.getElectionId() + "\" " + f1Disabled + ">Next</button>";
		}

	    out += "</div>";
	    out += "</div>";
	    out += "</form>";		
		
		return out;
	}
	
	
	public String drawVotingInterfaceForElectionStep2(VoteDto vote) {
		String out = "";
		
		if(vote == null) {
			return out;			
		}

		out += "<fieldset>";
		out += "<legend>Step 2: Vote Confirmation</legend>";
		out += "<div class=\"row\">";

		out += "<label>Encrypted Vote</label>";
		out += "<textarea name=\"text_cipher\" style=\"overflow:auto;resize:vertical\" readonly>" + vote.getVoteEncrypted() + "</textarea>";
		out += "</div>";

		out += "<div class=\"row\">";

		out += "<label>Signature</label>";
		out += "<textarea name=\"text_signature\" style=\"overflow:auto;resize:vertical\" placeholder=\"Enter your signature here...\"></textarea>";
		out += "</div>";
					
		out += "</fieldset>";		

		out += "<button class=\"button alert left\" type=\"submit\" name=\"button_vote\" value=\"" + vote.getElectionId() + "\">Back</button>";
		out += "<button class=\"button success right\" type=\"submit\" name=\"button_submit_vote\" value=\"" + vote.getElectionId() + "\">Vote!</button>";	

		return out;
	}
	
	
	
	
	
	
	
	
	public String drawElectionsTableForVoting(ArrayList<ElectionDto> elections) {
			String out = "";

			if(elections != null && elections.size() != 0) {
				out += "<h5>List of elections to vote</h5>";
				out += "<h5><form action=\"voting\" method=\"post\">";
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
					out += "<td><button class=\"label success\" type=\"submit\" name=\"button_vote\" value=\"" + e.getElectionId() + "\">vote</button></td>";
					out += "</tr>";
					i++;
				}
				
				out += "</tbody></table>";
				out += "</form>";
				
			} else {
				out += "<div class=\"label secondary\">No elections available yet</div>";
			}
			
			return out;
		}

	/**
	 * This function returns HTML code for alert
	 * @param message
	 * @param mode
	 * @return
	 */
	public String drawMessageAlert(String message, String mode) {
		String out = "";

		if(!message.equals("")) {
			out += "<div data-alert class=\"alert-box radius " + mode + "\">";
			out += "<b>" + message + "</b>";
			out += "<a href=\"\" class=\"close\">x</a>";
			out += "</div>";
		}
		
		return out;
	}
	
	/**
	 * This function returns HTML code for label
	 * @param message
	 * @param mode
	 * @return
	 */
	public String drawMessageLabel(String message, String mode) {
		String out = "";

		if(!message.equals("")) {
		  out += "<span class=\"label " + mode + "\">" + message + "</span>";
		}
		
		return out;
	}

		
		
		
		
		
		
		
	}
	
	
	
	

