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
import service.LoginService;
import service.TallyingService;
import service.VotingService;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

import dto.CandidateDto;
import dto.ElectionDto;
import dto.ElectionProgressDto;
import dto.Validator;
import enumeration.ElectionStatus;
import enumeration.ElectionType;
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
	private String placeHoldElecEndTime = "Enter election end time here";
	private String placeHoldElecCand = "Enter candidates names, one per line, candidates will appear in the same order as you specify.";
	private String placeHoldElecUsers = "Enter users emails, one per line.";
	
       
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
			} else if (request.getParameter("btn_elec_add_users") != null) {
				// SHOW USERS SCREEN
				routineElectionUsersModal(request);
			} else if (request.getParameter("btn_elec_save_users") != null) {
				routineElectionUsers(request);
			} else if (request.getParameter("btn_elec_open") != null ||
					   request.getParameter("btn_elec_close") != null ||
				       request.getParameter("btn_elec_publish") != null ) {
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
			out += "<th>Action</th>";
			out += "</tr></thead><tbody>";

			for (ElectionDto e : elections) {
				int voted = 0;
				
				Validator v2 = TallyingService.voteProgressStatusForElection(e.getElectionId());
				if(v2.isVerified()) {
					ElectionProgressDto epd = (ElectionProgressDto) v2.getObject();
					voted = epd.getTotalVotes();
				}
				
				String trClass = getElectionTableRowClass(e.getStatus());
				
				out += "<tr class =\"" + trClass + "\">";
				out += "<td class =\"" + trClass + "\">" + e.getElectionName() + "</td>";
				out += "<td class =\"" + trClass + "\">" + drawElectionStatus(e.getStatus(), e.getStatusDescription()) + "</td>";
				out += "<td class =\"" + trClass + "\">" + voted + " votes</td>";
				out += "<td class =\"" + trClass + "\">" + drawElectionAction(e.getElectionId(), e.getStatus(), e.getElectionType()) + "</td>";
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
		String out = "", valElecName = "", valElecDesc = "", valElecCand = "", 
			   valElecEndTime = "", valRegEmails = "void", valUnRegEmails = "", 
			   valPasswordErrorMessage = "", usersStyle = "";
		boolean valEmailListError = false, valPasswordError = false;
		int valElecId = 0, valElecAvailability = 0;
		
		// checking null case
		if(e != null) {
			valElecId = e.getElectionId();
			valElecName = e.getElectionName();
			valElecDesc = e.getElectionDescription();
			valElecCand = e.getCandidatesListString();
			valElecEndTime = e.getCloseDatetime();
			valElecAvailability = e.getElectionType();
			valRegEmails = e.getRegisteredEmailList();
			valEmailListError = e.isEmailListError();
			valUnRegEmails = (valEmailListError) ? "The following users could not be added: " + e.getUnregisteredEmailList() : "";
			valPasswordError = e.isPasswordError();
			valPasswordErrorMessage = e.getPasswordErrorMessage();
		}
		
		usersStyle = (valElecAvailability == ElectionType.PRIVATE.getCode()) ? "" : "display: none;";
		out += "<h5>Add new election</h5>";
		out += "<form id=\"form_elec_new\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Election Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("new_election_name", "Election Name", placeHoldElecName, valElecName);
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_description", "Election Description", placeHoldElecDesc, valElecDesc, false, "");
		out += HtmlService.drawInputTextAlphanumericOptional("new_election_end_time", "Election Closing Time", placeHoldElecEndTime, valElecEndTime);
		out += "</fieldset>";
		// password		
		out += "<fieldset><legend>Protect election by password</legend>";
		out += HtmlService.drawInputTextPasswordAndConfirmation("new_election_password", "Election Password", "new_election_password_confirm", "Confirm Election Password");		
		out += "</fieldset>"; 
		out += "</div>";

		out += "<div class=\"large-6 medium-6 columns\">";
		// candidates
		out += "<fieldset><legend>Add candidates</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_candidates", "Candidates names", placeHoldElecCand, valElecCand, false, "");
		out += "</fieldset>";
		// public and private
		out += "<fieldset><legend>Election avalability</legend>";
		out += HtmlService.drawSelectBoxElectionPrivateOrPublic("new_election_availability", valElecAvailability);
		// draw allowed users info
		out += "<div id=\"new_election_users_column\" style=\"" + usersStyle + "\">";
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_users", "Users emails", placeHoldElecUsers, valRegEmails, valEmailListError, valUnRegEmails);
		out += "</div>";
		out += "</fieldset>"; 
		out += "</div>";
		out += "</div>";
		// button and password
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button center\" type=\"submit\" name=\"save_new_election\" value=\"" + valElecId + "\">Save Election</button>";
		out += "</div>";
		out += "</div>";
		out += "</form>";
		
		return out; 
	}

	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for editing election modal
	 * @param e
	 * @return
	 */
	public String drawEditElection(ElectionDto e) {
		String out = "", valElecName = "", valElecDesc = "", valElecCand = "", 
			   valElecEndTime = "", valRegEmails = "", valUnRegEmails = "", usersStyle = "";
		int valElecId = 0, valElecAvailability = 0;
		boolean valEmailListError = false;
		// checking null case
		if(e != null) {
			valElecId = e.getElectionId();
			valElecName = e.getElectionName();
			valElecDesc = e.getElectionDescription();
			valElecCand = e.getCandidatesListString();
			valElecEndTime = e.getCloseDatetime();
			valElecAvailability = e.getElectionType();
			valRegEmails = e.getRegisteredEmailList();
			valEmailListError = e.isEmailListError();
			valUnRegEmails = (valEmailListError) ? "The following users could not be added: " + e.getUnregisteredEmailList() : "";
		}

		usersStyle = (valElecAvailability == ElectionType.PRIVATE.getCode()) ? "" : "display: none;";
		out += "<h5>Edit election</h5>";
		out += "<form id=\"form_elec_edit\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Election Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("edit_election_name", "Election Name", placeHoldElecName, valElecName);
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_description", "Election Description", placeHoldElecDesc, valElecDesc, false, "");
		out += HtmlService.drawInputTextAlphanumericOptional("edit_election_end_time", "Election Closing Time", placeHoldElecEndTime, valElecEndTime);
		out += HtmlService.drawSelectBoxElectionPrivateOrPublic("edit_election_availability", valElecAvailability);
		out += "</fieldset>";
		out += "<fieldset><legend>Add candidates</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_candidates", "Candidates names", placeHoldElecCand, valElecCand, false, "");
		out += "</fieldset>"; 

		out += "</div>";
		// draw allowed users info
		out += "<div id=\"edit_election_users_column\" style=\"" + usersStyle + "\" class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Invite users to vote</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_users", "Users emails", placeHoldElecUsers, valRegEmails, valEmailListError, valUnRegEmails);
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

		return out;
	}


	public String drawElectionUsers(ElectionDto e) {
		String out = "", valElecName = "", valEmailList = "", valRegEmails = "",
			   valUnRegEmails = "", valEmailListErrorMessage = "";
			int valElecId = 0;
			boolean valEmailListError = false;
			// checking null case
			if(e != null) {
				valElecId = e.getElectionId();
				valElecName = e.getElectionName();
				valEmailList = e.getEmailList();
				valRegEmails = e.getRegisteredEmailList();		
				valEmailListError = e.isEmailListError();
				valEmailListErrorMessage = e.getEmailListMessage();
				valUnRegEmails = (valEmailListError) ? "The following users could not be added: " + e.getUnregisteredEmailList() : "";
			}

			out += "<h5>" + valElecName + "</h5>";
			out += "<form id=\"form_elec_edit\" action=\"election\" method=\"post\" data-abide>";
			out += "<div class=\"row\">";
			// draw election info
			out += "<div class=\"large-6 medium-6 columns\">";
			out += "<fieldset><legend>Existing Users</legend>";
			out += HtmlService.drawInputTextareaReadonly("edit_election_users", "Existing users", "No users", valEmailList);
			out += "</fieldset>";
			out += "</div>";

			// draw allowed users info
			out += "<div id=\"edit_election_users_column\" class=\"large-6 medium-6 columns\">";

			out += "<fieldset><legend>Invite additional users to vote</legend>";
			out += HtmlService.drawInputTextareaAlphanumeric("edit_election_new_users", "Users emails", placeHoldElecUsers, valRegEmails, valEmailListError, valUnRegEmails);
			out += "</fieldset>"; 
			out += "</div>";
			out += "</div>";
			// button
			out += "<div class=\"row\">";
			out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
			out += "<button class=\"radius button right\" type=\"submit\" name=\"save_edit_election_users\" value=\"" + valElecId + "\">Save Election</button>";
			out += "</div>";
			out += "</div>";
			out += "</form>";

			return out;

		
		
	}
	
	
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for action for given election and it's status
	 * @param electionId
	 * @param statusId
	 * @return
	 */
	public String drawElectionAction(int electionId, int statusId, int electionTypeId) {
		String out = "";
		
		if(statusId == ElectionStatus.NEW.getCode()) {
			out += "<button class=\"label radius\" type=\"submit\" name=\"btn_elec_open\" value=\"" + electionId + "\">Open</button>";
			out += " / ";
			out += "<button class=\"label radius\" type=\"submit\" name=\"election\" value=\"" + electionId + "\">Edit</button>";
		} else if(statusId == ElectionStatus.OPEN.getCode()) {
			out += "<button class=\"label radius\" type=\"submit\" name=\"btn_elec_close\" value=\"" + electionId + "\">Close</button>";
			if(electionTypeId == ElectionType.PRIVATE.getCode()) {
				out += " / ";
				out += "<button class=\"label radius\" type=\"submit\" name=\"btn_elec_add_users\" value=\"" + electionId + "\">Add users</button>";
			}
		} else if(statusId == ElectionStatus.CLOSED.getCode()) {
			out += "<button class=\"label radius\" type=\"submit\" name=\"btn_elec_open\" value=\"" + electionId + "\">Reopen</button>";
			out += " / ";
			out += "<button class=\"label radius\" type=\"submit\" name=\"btn_elec_publish\" value=\"" + electionId + "\">Publish</button>";
		} else if(statusId == ElectionStatus.PUBLISHED.getCode()) {
			out += "";
		}

		return out;
	}

	
	public String getElectionTableRowClass(int statusId) {
		String out = "";
		
		if(statusId == ElectionStatus.NEW.getCode()) {
			out += "election_new";
		} else if(statusId == ElectionStatus.OPEN.getCode()) {
			out += "election_open";
		} else if(statusId == ElectionStatus.CLOSED.getCode()) {
			out += "election_closed";
		} else if(statusId == ElectionStatus.PUBLISHED.getCode()) {
			out += "election_published";
		}
		
		return out;
	}
	
		
	/**
	 * Dmitriy Karmazin
	 * @param status - current status of election
	 * @param value - string representation of status
	 * @return
	 */
	public String drawElectionStatus(int status, String value) {
		String out = "", outClass="";
		
		if(status == ElectionStatus.NEW.getCode()) {
			outClass = "label clear";
		} else if(status == ElectionStatus.OPEN.getCode()) {
			outClass = "label clear";
		} else if(status == ElectionStatus.CLOSED.getCode()) {
			outClass = "label clear";
		} else if(status == ElectionStatus.PUBLISHED.getCode()) {
			outClass = "label clear";
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
			out += "<button class=\"label button_link radius\" type=\"submit\" name=\"election\" value=\"" + electionId + "\">Edit</button></td>";
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
		newElection.setCloseDatetime(request.getParameter("new_election_end_time"));
		newElection.setElectionType(Integer.parseInt(request.getParameter("new_election_availability")));
		newElection.setEmailList(request.getParameter("new_election_users"));
		String password = request.getParameter("new_election_password");

		// password checked
		newElection.setOwnerId(HeaderService.getUserId());
		// insert attempt
		Validator vElection = ElectionService.addElection(newElection);

		if (vElection.isVerified()) {
			 //insert was successful
			 mode = "1";
			 messageAlert = HtmlService.drawMessageAlert(vElection.getStatus(), "success");
		} else {
			// errors, send back to add election screen
			mode = "2";
			messageLabel = HtmlService.drawMessageLabel(vElection.getStatus(), "alert");
			outModal = drawNewElection((ElectionDto)vElection.getObject());
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
		editElection.setCloseDatetime(request.getParameter("edit_election_end_time"));
		editElection.setElectionType(Integer.parseInt(request.getParameter("edit_election_availability")));
		editElection.setRegisteredEmailList(request.getParameter("edit_election_users"));
		editElection.setOwnerId(HeaderService.getUserId());

		// update existing election
		Validator vEditElection = ElectionService.editElection(editElection);

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
		// get election id
		int electionId = 0, action = -1;
		if(request.getParameter("btn_elec_open") != null) {
			electionId = Integer.parseInt(request.getParameter("btn_elec_open"));
			action = ElectionStatus.OPEN.getCode();
		} else if(request.getParameter("btn_elec_close") != null) {
			electionId = Integer.parseInt(request.getParameter("btn_elec_close"));
		} else if(request.getParameter("btn_elec_publish") != null) {
			electionId = Integer.parseInt(request.getParameter("btn_elec_publish"));
			action = ElectionStatus.PUBLISHED.getCode();
		}
		
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
			} else if (e.getStatus() == ElectionStatus.CLOSED.getCode() && action == ElectionStatus.OPEN.getCode()) {
				// reopen
				Validator v4 = ElectionService.reOpenElection(electionId);
				messageAlert = HtmlService.drawMessageAlert(
						v4.getStatus(), "success");
			} else if (e.getStatus() == ElectionStatus.CLOSED.getCode() && action == ElectionStatus.PUBLISHED.getCode()) {
				// publish
				Validator v5 = ElectionService.publishResults(electionId);
				messageAlert = HtmlService.drawMessageAlert(
						v5.getStatus(), "success");
			}
		} else {
			messageAlert = HtmlService.drawMessageAlert(
					v1.getStatus(), "alert");
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs all routines required to display users editing modal
	 * @param request
	 */
	public void routineElectionUsersModal(HttpServletRequest request) {
		resetGlobals();
		int electionId = 0;		
		if(request.getParameter("btn_elec_add_users") != null) {
			electionId = Integer.parseInt(request.getParameter("btn_elec_add_users"));
		}

		Validator vElectionUsers = ElectionService.selectElection(electionId);

		if (vElectionUsers.isVerified()) {
			ElectionDto editElection = (ElectionDto) vElectionUsers.getObject();
			this.mode = "2";
			this.outModal = drawElectionUsers(editElection);
		} else {
			messageAlert = HtmlService.drawMessageAlert(vElectionUsers.getStatus(), "alert");
		}
	}	
	
	
	public void routineElectionUsers(HttpServletRequest request) {
		resetGlobals();
		int electionId = 0;		
		if(request.getParameter("save_edit_election_users") != null) {
			electionId = Integer.parseInt(request.getParameter("save_edit_election_users"));
		}

		// prepare new election
		ElectionDto newElection = new ElectionDto();		
		newElection.setRegisteredEmailList(request.getParameter("edit_election_new_users"));

		// password checked
		newElection.setOwnerId(HeaderService.getUserId());
		// insert attempt
		Validator vElection = ElectionService.editElection(newElection);

		if (vElection.isVerified()) {
			 //insert was successful
			 mode = "1";
			 messageAlert = HtmlService.drawMessageAlert(vElection.getStatus(), "success");
		} else {
			// errors, send back to add election screen
			mode = "2";
			messageLabel = HtmlService.drawMessageLabel(vElection.getStatus(), "alert");
			outModal = drawNewElection(newElection);
		}
	}
}
