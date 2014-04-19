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
			} else if (request.getParameter("save_edit_election_users") != null) {
				routineElectionUsers(request);
			} else if (request.getParameter("btn_elec_open") != null ||
					   request.getParameter("btn_elec_close") != null) {
				// PERFORM ACTION ON ELECTION
				routineActionOnElection(request);
			} else if(request.getParameter("btn_elec_publish") != null) {
				// PUBLISH BUTTON PRESSED
				routinePublishElectionModal(request);
			} else if(request.getParameter("btn_elec_publish_with_password") != null) {
				// PUBLISH BUTTON PRESSED
				routinePublishElection(request);
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
			   valElecEndTime = "", valRegEmails = "", valUnRegEmails = "", 
			   valPasswordErrorMessage = "", usersFieldStyle = "";
		boolean valEmailListError = false, valPasswordError = false, valRequired = true;
		int valElecId = 0, valElecAvailability = ElectionType.PRIVATE.getCode();
		
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
			valUnRegEmails = (valEmailListError) ? e.getUnregisteredEmailList() : "";
			valPasswordError = e.isPasswordError();
			valPasswordErrorMessage = e.getPasswordErrorMessage();
		}
		
		if(valElecAvailability != ElectionType.PRIVATE.getCode()) {
			usersFieldStyle = "style=\"display: none;\"";
			valRequired = false;
		}
		
		out += "<form id=\"form_elec_new\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		out += "<h5>Add new election</h5>";

		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Election Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("new_election_name", "Election Name", placeHoldElecName, valElecName);
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_description", "Election Description", placeHoldElecDesc, valElecDesc, false, "", true);
		out += HtmlService.drawInputTextAlphanumericOptional("new_election_end_time", "Election Closing Time", placeHoldElecEndTime, valElecEndTime);
		out += "</fieldset>";
		// password		
		out += "<fieldset><legend>Protect election by password <span data-tooltip class=\"has-tip tip-top\" title=\"Steven, please provide the message.\">what is this?</span></legend>";
		out += HtmlService.drawInputTextPasswordAndConfirmation("new_election_password", "Election Password", "new_election_password_confirm", "Confirm Election Password");		
		out += "</fieldset>"; 
		out += "</div>";
		// column 2
		out += "<div class=\"large-6 medium-6 columns\">";
		// candidates
		out += "<fieldset><legend>Add candidates</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_candidates", "Candidates names", placeHoldElecCand, valElecCand, false, "", true);
		out += "</fieldset>";
		// public and private
		out += "<fieldset><legend>Election avalability</legend>";
		out += HtmlService.drawSelectBoxElectionPrivateOrPublic("new_election_availability", valElecAvailability);
		// draw allowed users info
		out += "<div id=\"new_election_users_column\" " + usersFieldStyle + ">";
		out += HtmlService.drawInputTextareaAlphanumeric("new_election_users", "Users emails", placeHoldElecUsers, valRegEmails, valEmailListError, "Not all users are registered in the system.", valRequired);
		out += "</div>";
		// draw failed users select
		out += "<div id=\"new_election_users_invite\" " + usersFieldStyle + ">";
		out += HtmlService.drawCheckBoxesElectionPrivateOrPublic("new_election_users_invited", valEmailListError, valUnRegEmails);
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
		boolean valEmailListError = false, valRequired = true;
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
			valUnRegEmails = (valEmailListError) ? e.getUnregisteredEmailList() : "";
		}

		if(valElecAvailability != ElectionType.PRIVATE.getCode()) {
			usersStyle = "style=\"display: none;\"";
			valRequired = false;
		}
		
		out += "<form id=\"form_elec_edit\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		out += "<h5>Edit election</h5>";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Election Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("edit_election_name", "Election Name", placeHoldElecName, valElecName);
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_description", "Election Description", placeHoldElecDesc, valElecDesc, false, "", true);
		out += HtmlService.drawInputTextAlphanumericOptional("edit_election_end_time", "Election Closing Time", placeHoldElecEndTime, valElecEndTime);
		out += "</fieldset>";
		out += "<fieldset><legend>Add candidates</legend>";
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_candidates", "Candidates names", placeHoldElecCand, valElecCand, false, "", true);
		out += "</fieldset>";
		out += "</div>";
		// draw allowed users info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset><legend>Election avalability</legend>";
		out += HtmlService.drawSelectBoxElectionPrivateOrPublic("edit_election_availability", valElecAvailability);
		// draw allowed users info
		out += "<div id=\"edit_election_users_column\" " + usersStyle + ">";
		out += HtmlService.drawInputTextareaAlphanumeric("edit_election_users", "Users emails", placeHoldElecUsers, valRegEmails, valEmailListError, "Not all users are registered in the system.", valRequired);
		out += "</div>";
		out += "<div id=\"edit_election_users_invite\" " + usersStyle + ">";
		out += HtmlService.drawCheckBoxesElectionPrivateOrPublic("edit_election_users_invited", valEmailListError, valUnRegEmails);
		out += "</div>";
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
		String out = "", valElecName = "", valCurrentEmailList = "", valRegEmails = "",
			   valUnRegEmails = "", valEmailListErrorMessage = "";
			int valElecId = 0;
			boolean valEmailListError = false;
			// checking null case
			if(e != null) {
				valElecId = e.getElectionId();
				valElecName = e.getElectionName();
				valCurrentEmailList = e.getCurrentEmailList();
				valRegEmails = e.getRegisteredEmailList();
				valEmailListError = e.isEmailListError();
				valEmailListErrorMessage = e.getEmailListMessage();
				valUnRegEmails = (valEmailListError) ? "The following users could not be added: " + e.getUnregisteredEmailList() : "";
			}

			out += "<h5>Add users to private election: " + valElecName + "</h5>";
			out += "<form id=\"form_elec_edit\" action=\"election\" method=\"post\" data-abide>";
			out += "<div class=\"row\">";
			// draw election info
			out += "<div class=\"large-6 medium-6 columns\">";
			out += "<fieldset><legend>Existing Users</legend>";
			out += HtmlService.drawInputTextareaReadonly("edit_election_users", "Existing users", "No users", valCurrentEmailList);
			out += "</fieldset>";
			out += "</div>";

			// draw allowed users info
			out += "<div id=\"edit_election_users_column\" class=\"large-6 medium-6 columns\">";

			out += "<fieldset><legend>Invite additional users to vote</legend>";
			out += HtmlService.drawInputTextareaAlphanumeric("edit_election_new_users", "Users emails", placeHoldElecUsers, valRegEmails, valEmailListError, valUnRegEmails, true);
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
		Validator v = ElectionService.selectElectionsForOwner(HeaderService.getUserId());
		
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
		newElection.setPassword(request.getParameter("new_election_password"));
		newElection.setEmailListInvited(getStringFromArray(request.getParameterValues("new_election_users_invited")));
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
		Validator vEditElection = ElectionService.selectElectionForOwner(electionId);

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
		
		String usersToInvite = getStringFromArray(request.getParameterValues("edit_election_users_invited"));
		System.out.println(usersToInvite);
		
		ElectionDto editElection = new ElectionDto();
		editElection.setElectionId(Integer.parseInt(request.getParameter("save_edit_election")));
		editElection.setElectionName(request.getParameter("edit_election_name"));
		editElection.setElectionDescription(request.getParameter("edit_election_description"));
		editElection.setCandidatesListString(request.getParameter("edit_election_candidates"));
		editElection.setCloseDatetime(request.getParameter("edit_election_end_time"));
		editElection.setElectionType(Integer.parseInt(request.getParameter("edit_election_availability")));
		editElection.setEmailList(request.getParameter("edit_election_users"));
		editElection.setEmailListInvited(getStringFromArray(request.getParameterValues("new_election_users_invited")));
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
			this.outModal = drawEditElection((ElectionDto) vEditElection.getObject());
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs required actions to show add new election modal
	 */
	public void routineAddNewElectionModal() {	
		resetGlobals();
		mode = "2";
		messageLabel = HtmlService.drawMessageLabel("Please fill in required fields", "secondary");
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
		
		Validator v1 = ElectionService.selectElectionForOwner(electionId);		
		if (v1.isVerified()) {
			ElectionDto e = (ElectionDto) v1.getObject();

			if (e.getStatus() == ElectionStatus.NEW.getCode()) {
				// open election
				Validator v2 = ElectionService.openElection(electionId);
				messageAlert = HtmlService.drawMessageAlert(
						v2.getStatus(), "");
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

		Validator vElectionUsers = ElectionService.selectElectionFullDetail(electionId);

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
		newElection.setElectionId(electionId);
		newElection.setEmailList(request.getParameter("edit_election_new_users"));
		newElection.setOwnerId(HeaderService.getUserId());
		// insert attempt
		Validator vElection = ElectionService.addAdditionalUsersToElection(newElection);

		if (vElection.isVerified()) {
			 //insert was successful
			 mode = "1";
			 messageAlert = HtmlService.drawMessageAlert(vElection.getStatus(), "success");
		} else {
			// errors, send back to add election screen
			mode = "2";
			messageLabel = HtmlService.drawMessageLabel(vElection.getStatus(), "alert");
			outModal = drawElectionUsers((ElectionDto) vElection.getObject());
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performs all activities to display modal before publishing election results
	 * @param request
	 */
	public void routinePublishElectionModal(HttpServletRequest request) {
		resetGlobals();

		int electionId = 0;		
		if(request.getParameter("btn_elec_publish") != null) {
			electionId = Integer.parseInt(request.getParameter("btn_elec_publish"));
		}

		Validator vEditElection = ElectionService.selectElectionForOwner(electionId);
		if (vEditElection.isVerified()) {
			this.mode = "2";
			this.outModal = drawPasswordWithConfirmForElection((ElectionDto) vEditElection.getObject());
		} else {
			messageAlert = HtmlService.drawMessageAlert(vEditElection.getStatus(), "alert");
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns HTML output for password modal
	 * @param e
	 * @return
	 */
	public String drawPasswordWithConfirmForElection(ElectionDto e) {
		String out = "", valElecName = "", valPasswordErrorMessage = "";
		int valElecId = 0;
		boolean valPasswordError = false;

		// checking null case
		if(e != null) {
			valElecId = e.getElectionId();
			valElecName = e.getElectionName();
			valPasswordError = e.isPasswordError();
			valPasswordErrorMessage = (valPasswordError) ? "Invalid password" : "";
		}		
		
		out += "<form id=\"form_elec_new\" action=\"election\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 large-centered medium-6 medium-centered columns\">";
		out += "<h5>Publish Election Results: " + valElecName + "</h5>";
		out += "<fieldset><legend>Please Enter Election Password</legend>";
		out += "<p class=\"global_warning\">Warning! Election publishing is irrevocable action.<br>The results of this election will become available to voters and you will not be able to reopen this election anymore.<br> Enter election password and click 'Publish Election' to proceed or click 'Cancel Publishing' otherwise.</p>";
		
		out += HtmlService.drawInputTextPassword("election_publish_password", "Election Password", "", "", valPasswordError, valPasswordErrorMessage);
		out += "</fieldset>"; 		
		out += "<button class=\"radius button left\" type=\"submit\" name=\"btn_elec_publish_with_password\" value=\"" + valElecId + "\">Publish Election</button>";
		out += "<button class=\"button alert right\" type=\"button\" id=\"btn_elec_publish_cancel\" \">Cancel Publishing</button>";
		out += "</div>";
		out += "</div>";
		out += "</form>";

		return out;
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function performes routines required for publishing an election
	 * @param request
	 */
	public void routinePublishElection(HttpServletRequest request) {
		resetGlobals();

		int electionId = 0;
		String password = "";
		if(request.getParameter("btn_elec_publish_with_password") != null) {
			electionId = Integer.parseInt(request.getParameter("btn_elec_publish_with_password"));
		}
		if(request.getParameter("election_publish_password") != null) {
			password = request.getParameter("election_publish_password");
		}

		Validator v1 = ElectionService.publishResults(electionId, password);

		if(v1.isVerified()) {
			messageAlert = HtmlService.drawMessageAlert("Election published", "success");					
		} else {
			mode = "2";
			outModal = drawPasswordWithConfirmForElection((ElectionDto) v1.getObject());
			messageLabel = HtmlService.drawMessageLabel(v1.getStatus(), "alert");
		}
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function returns string out of given array
	 * @param arrayOfStrings
	 * @return
	 */
	public String getStringFromArray(String[] arrayOfStrings) {
		String out = "";
		String deliminter = System.getProperty("line.separator");

		if(arrayOfStrings != null) {
			for(String str : arrayOfStrings) {
				out += str + deliminter;
			}
		}
		
		return out;
	}
}
