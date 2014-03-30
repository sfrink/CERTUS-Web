package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.Initializer;
import service.CandidateService;
import service.HtmlService;
import service.ElectionService;
import service.HeaderService;
import service.TallyingService;
import service.VotingService;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

import dto.CandidateDto;
import dto.ElectionDto;
import dto.ElectionProgressDto;
import dto.Validator;
import enumeration.ElectionStatus;
import enumeration.Status;

/**
 * Servlet implementation class ElectionServlet
 */
@WebServlet(name = "election", urlPatterns = { "/election" })
public class ElectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	private String outElections = "";
	private String placeHoldElecName = "Enter election name here";
	private String placeHoldElecDesc = "Enter election description here";
	private String placeHoldElecCand = "Enter candidates names, one per line";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ElectionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check authentication
		if(HeaderService.isAuthenticated()) {
			resetGlobals();
			routineExistingElections();
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/election.jsp");
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

			if (request.getParameter("button_add_election") != null) {
				// SHOW NEW ELECTION SCREEN
				routineAddNewElectionModal();
			} else if (request.getParameter("save_new_election") != null) {
				// ADDING A NEW ELECTION
				routineAddNewElection(request);
			} else if (request.getParameter("election") != null) {
				// SHOW EDIT ELECTION SCREEN
				routineEditElectionModal(request);				
			} else if (request.getParameter("save_edit_election") != null) {
				// SAVE EDITED ELECTION
				routineEditElectionSave(request);
			} else if (request.getParameter("button_election_action") != null) {
				// PERFORM ACTION ON ELECTION
				routineActionOnElection(request);
			}
			// refresh existing elections
			routineExistingElections();

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(
					"/election.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}
	
	
	/**
	 * This function returns HTML output for all existing elections
	 * @param elections
	 * @return
	 */
	public String drawExistingElections(ArrayList<ElectionDto> elections) {
		String out = "";
				
		if(elections != null && elections.size() != 0) {
			out += "<h5>Existing elections</h5>";
			out += "<form action=\"election\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>Election Name</th>";
			out += "<th>Election Status</th>";
			out += "<th>Votes Collected</th>";
			out += "<th>Edit</th>";
			out += "</tr></thead><tbody>";
			
			for (ElectionDto e : elections) {
				int voted = 0;
				
				Validator v2 = TallyingService.voteProgressStatusForElection(e.getElectionId());
				if(v2.isVerified()) {
					ElectionProgressDto epd = (ElectionProgressDto) v2.getObject();
					voted = epd.getTotalVotes();
				}
				
				out += "<tr>";
				out += "<td>" + e.getElectionName() + "</td>";
				out += "<td>";
				out += drawElectionStatusColored(e.getStatus(), e.getStatusDescription()) + " ";
				out += drawElectionAction(e.getElectionId(), e.getStatus());
				out += "</td>";
				out += "<td>"+ voted + " votes</td>";
				out += "<td>" + drawElectionEdit(e.getElectionId(), e.getStatus()) + "</td>";
				out += "</tr>";
			}
			
			out += "</tbody></table></form>";
		} else {
			out += "<div class=\"label secondary\">No elections exist yet</div>";
		}
		
		return out;
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This method draws HTML modal output for a new election
	 * @param e - election object to draw 
	 * @return HTML output
	 */
	public String drawNewElection(ElectionDto e) {
		String out = "";
		int valElecId = 0;
		String valElecName = "", valElecDesc = "", valElecCand = "";
		// checking null case
		if(e != null) {
			valElecId = e.getElectionId();
			valElecName = e.getElectionName();
			valElecDesc = e.getElectionDescription();
			valElecCand = e.getCandidatesListString();
		}
				
		out += "<h5>Add new election</h5>";
		out += "<form id=\"form_elec_new\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Election Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("new_election_name", "Election Name", placeHoldElecName, valElecName);
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_description", "Election Description", placeHoldElecDesc, valElecDesc);
		out += "</fieldset>";
		out += "</div>";
		// draw candidates info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Add candidates</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_candidates", "Candidates names", placeHoldElecCand, valElecCand);
		out += "</fieldset>"; 
		out += "</div>";
		out += "</div>";
		// button
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"save_new_election\" value=\"" + valElecId + "\">Save Election</button>";
		out += "</div>";
		out += "</div>";
		out += "</form>";
		out += "<a class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for editing election modal
	 * @param e
	 * @return
	 */
	public String drawEditElection(ElectionDto e) {
		String out = "";
		int valElecId = 0;
		String valElecName = "", valElecDesc = "", valElecCand = "";
		// checking null case
		if(e != null) {
			valElecId = e.getElectionId();
			valElecName = e.getElectionName();
			valElecDesc = e.getElectionDescription();
			valElecCand = e.getCandidatesListString();
		}

		out += "<h5>Edit election</h5>";
		out += "<form id=\"form_elec_edit\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Election Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("edit_election_name", "Election Name", placeHoldElecName, valElecName);
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_description", "Election Description", placeHoldElecDesc, valElecDesc);
		out += "</fieldset>";
		out += "</div>";
		// draw candidates info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>Add candidates</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_candidates", "Candidates names", placeHoldElecCand, valElecCand);
		out += "</fieldset>"; 
		out += "</div>";
		out += "</div>";
		// button
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"save_edit_election\" value=\"" + valElecId + "\">Save Election</button>";
		out += "</div>";
		out += "</div>";
		out += "</form>";
		out += "<a class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for action for given election and it's status
	 * @param electionId
	 * @param statusId
	 * @return
	 */
	public String drawElectionAction(int electionId, int statusId) {
		String out = "";
		
		if(statusId == ElectionStatus.NEW.getCode()) {
			out = "<button class=\"label success radius\" type=\"submit\" name=\"button_election_action\" value=\"" + electionId + "\">" + ElectionStatus.OPEN.getLabel() + "</button>";
		} else if(statusId == ElectionStatus.OPEN.getCode()) {
			out = "<button class=\"label alert radius\" type=\"submit\" name=\"button_election_action\" value=\"" + electionId + "\">" + ElectionStatus.CLOSED.getLabel() + "</button>";
		} else if(statusId == ElectionStatus.CLOSED.getCode()) {
			out = "";
		} else if(statusId == ElectionStatus.PUBLISHED.getCode()) {
			out = "";
		}

		return out;
	}

		
	/**
	 * Dmitriy Karmazin
	 * @param status - current status of election
	 * @param value - string representation of status
	 * @return
	 */
	public String drawElectionStatusColored(int status, String value) {
		String out = "", outClass="";
		
		if(status == ElectionStatus.NEW.getCode()) {
			outClass = "";
		} else if(status == ElectionStatus.OPEN.getCode()) {
			outClass = "label success";
		} else if(status == ElectionStatus.CLOSED.getCode()) {
			outClass = "label alert";
		} else if(status == ElectionStatus.PUBLISHED.getCode()) {
			outClass = "label";
		} else {
			return out;
		}
		
		out = "<span class=\"" + outClass + "\">" + value + "</span>";		
		return out;
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for edit button for an eletion
	 */
	public String drawElectionEdit(int electionId, int statusId) {
		String out = "";
		
		if(statusId == ElectionStatus.NEW.getCode()) {
			out += "<button class=\"label radius\" type=\"submit\" name=\"election\" value=\"" + electionId + "\">edit</button></td>";
		}
	
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
		this.outModal = "";
		this.outElections = "";
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function sets global variables to display existing elections
	 */
	public void routineExistingElections() {
		// get the list of elections from DB
		ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();
		Validator v = ElectionService.selectElectionsOwnedByUser(HeaderService.getUserId());
		
		if(v.isVerified()) {
			allElections = (ArrayList<ElectionDto>) v.getObject();	
		} else {
			messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert") ;
		}
		
		outElections = drawExistingElections(allElections);
	}	
	
	
	/**
	 * Dmitriy Karmazin
	 * This function makes required actions to insert a new election to DB
	 * @param request
	 */
	public void routineAddNewElection(HttpServletRequest request) {
		resetGlobals();
		
		// prepare new election
		ElectionDto newElection = new ElectionDto();
		newElection.setElectionName(request.getParameter("new_election_name"));
		newElection.setElectionDescription(request.getParameter("new_election_description"));
		newElection.setCandidatesListString(request.getParameter("new_election_candidates"));				
		newElection.setOwnerId(HeaderService.getUserId());
		// insert attempt
		Validator vElection = ElectionService.addElection(newElection);

		if (vElection.isVerified()) {
			// insert was successful
			mode = "1";
			messageAlert = HtmlService.drawMessageAlert(vElection.getStatus(), "success");
		} else {
			// errors, send back to add election screen
			mode = "2";
			messageLabel = HtmlService.drawMessageLabel(vElection.getStatus(), "alert");
			outModal = drawNewElection(newElection);
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs required routines to open edit modal
	 * @param request
	 */
	public void routineEditElectionModal(HttpServletRequest request) {
		resetGlobals();
		// get election by provided election id
		int electionId = Integer.parseInt(request.getParameter("election"));
		Validator vEditElection = ElectionService.selectElection(electionId);

		if (vEditElection.isVerified()) {
			ElectionDto editElection = (ElectionDto) vEditElection.getObject();
			this.mode = "2";
			this.outModal = drawEditElection(editElection);
		} else {
			messageAlert = HtmlService.drawMessageAlert(vEditElection.getStatus(), "alert");
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This performs routines required to update an existing election
	 * @param request
	 */
	public void routineEditElectionSave(HttpServletRequest request) {
		resetGlobals();
		
		ElectionDto editElection = new ElectionDto();
		editElection.setElectionId(Integer.parseInt(request.getParameter("save_edit_election")));
		editElection.setElectionName(request.getParameter("edit_election_name"));
		editElection.setElectionDescription(request.getParameter("edit_election_description"));
		editElection.setCandidatesListString(request.getParameter("edit_election_candidates"));
		editElection.setOwnerId(HeaderService.getUserId());

		// update existing election
		Validator vEditElection = ElectionService.addElection(editElection); // TBF

		if (vEditElection.isVerified()) {
			// insert of candidates was successful
			this.mode = "1";
			this.messageAlert = HtmlService.drawMessageAlert(
					vEditElection.getStatus(), "success");
		} else {
			// error, return to editing screen
			this.mode = "2";
			this.messageLabel = HtmlService.drawMessageLabel(
					vEditElection.getStatus(), "alert");
			this.outModal = drawEditElection(editElection);
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs required actions to show add new election modal
	 */
	public void routineAddNewElectionModal() {	
		resetGlobals();
		mode = "2";
		messageLabel = HtmlService.drawMessageLabel("Please fill in required labels", "secondary");
		outModal = drawNewElection(null);
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs a particular action on given election
	 * @param request
	 */
	public void routineActionOnElection(HttpServletRequest request) {
		resetGlobals();
		// Select election
		int electionId = Integer.parseInt(request.getParameter("button_election_action"));
		Validator v1 = ElectionService.selectElection(electionId);

		if (v1.isVerified()) {
			ElectionDto e = (ElectionDto) v1.getObject();

			if (e.getStatus() == ElectionStatus.NEW.getCode()) {
				// open election
				Validator v2 = ElectionService.openElection(electionId);
				messageAlert = HtmlService.drawMessageAlert(
						v2.getStatus(), "success");
			} else if (e.getStatus() == ElectionStatus.OPEN.getCode()) {
				// close election
				Validator v3 = ElectionService
						.closeElection(electionId);
				messageAlert = HtmlService.drawMessageAlert(
						v3.getStatus(), "success");
			}
		}
	}
}
